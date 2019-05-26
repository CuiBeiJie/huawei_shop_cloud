package com.huawei.search.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.huawei.common.enums.ExceptionEnums;
import com.huawei.common.exception.SelfException;
import com.huawei.common.utils.JsonUtils;
import com.huawei.common.vo.PageResult;
import com.huawei.item.pojo.*;
import com.huawei.item.vo.SpuVO;
import com.huawei.search.client.BrandClient;
import com.huawei.search.client.CategoryClient;
import com.huawei.search.client.GoodsClient;
import com.huawei.search.client.SpecificationClient;
import com.huawei.search.pojo.Goods;
import com.huawei.search.pojo.SearchRequest;
import com.huawei.search.pojo.SearchResult;
import com.huawei.search.repository.GoodsRepository;
import com.huawei.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: huaweishop
 * @description: 搜索服务实现层
 * @author: cuibeijie
 * @create: 2019-05-19 16:50
 */
@Service
@Slf4j
public class SearchServiceImpl implements SearchService {
    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 根据spu导入Goods数据
     *
     * @param spu
     * @return
     */
    public Goods buildGoods(SpuVO spu) {
        //查询分类
        List<Category> categories = categoryClient.queryCategoryListByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        if (CollectionUtils.isEmpty(categories)) {
            throw new SelfException(ExceptionEnums.CATEGORY_NOT_FOUND);
        }
        //分类名称集合
        List<String> categorieNameList = categories.stream().map(Category::getName).collect(Collectors.toList());

        //查询品牌
        Brand brand = brandClient.queryBrandByid(spu.getBrandId());
        if (brand == null) {
            throw new SelfException(ExceptionEnums.BRAND_NOT_FOUND);
        }
        //搜索字段 拼接：标题、分类、品牌
        String all = spu.getTitle() + StringUtils.join(categorieNameList, " ") + brand.getName();

        //查询sku
        List<Sku> skuList = goodsClient.querySkuBySpuId(spu.getId());
        if (CollectionUtils.isEmpty(skuList)) {
            throw new SelfException(ExceptionEnums.GOODS_NOT_FOUND);
        }
        //对sku进行处理(页面展示不需要sku的全部属性)
        List<Map<String, Object>> skus = new ArrayList<>();
        for (Sku sku : skuList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("price", sku.getPrice());
            map.put("image", StringUtils.substringBefore(sku.getImages(), ","));
            skus.add(map);
        }
        //处理价格
        Set<Long> priceList = skuList.stream().map(Sku::getPrice).collect(Collectors.toSet());

        // 查询规格参数
        List<SpecParam> specParamsList = specificationClient.querySpecParamList(null, spu.getCid3(), true);
        if (CollectionUtils.isEmpty(specParamsList)) {
            throw new SelfException(ExceptionEnums.SPECPARM);
        }
        //查询商品详情
        SpuDetail spuDetail = goodsClient.querySpuDetailById(spu.getId());
        //获取通用规格参数
        Map<Long, String> genericSpec = JsonUtils.parseMap(spuDetail.getGenericSpec(), Long.class, String.class);
        //获取特有规格参数
        Map<Long, List<String>> specialSpec = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {
        });
        //规格参数，key是规格参数的名称，值是规格参数的值
        Map<String, Object> specs = new HashMap<>();
        for (SpecParam specParam : specParamsList) {
            String key = specParam.getName();
            if (specParam.getGeneric()) {
                //通用参数
                String value = genericSpec.get(specParam.getId());
                // 数值类型，需要存储一个分段
                if (specParam.getNumeric()) {
                    value = chooseSegment(value, specParam);
                }
                specs.put(key, value);
            } else {
                //特有参数
                //存入map
                specs.put(key, specialSpec.get(specParam.getId()));
            }

        }

        //构建Goods对象
        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(spu.getId());
        goods.setSubTitle(spu.getSubTitle());
        // 搜索条件
        goods.setAll(all);
        //所有sku的价格集合
        goods.setPrice(priceList);
        //所有sku集合的json格式
        goods.setSkus(JsonUtils.serialize(skus));
        //所有可搜索的规格参数
        goods.setSpecs(specs);
        return goods;
    }

    /**
     * 搜索
     *
     * @param request
     * @return
     */
    public PageResult<Goods> search(SearchRequest request) {
        String key = request.getKey();
        if (StringUtils.isBlank(key)) {
            // 如果用户没搜索条件，我们可以给默认的，或者返回null
            return null;
        }

        Integer page = request.getPage() - 1;// page 从0开始
        Integer size = request.getSize();

        // 1、创建查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 2、查询
        // 2.1、对结果进行筛选
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"}, null));
        // 2.2、基本查询
        QueryBuilder baseQuery = buildBasicQueryWithFilter(request);
        queryBuilder.withQuery(baseQuery);

        // 2.3、分页
        queryBuilder.withPageable(PageRequest.of(page, size));

        //2.4、聚合分类和品牌
        String categoryAggName = "category_agg"; // 商品分类聚合名称
        String brandAggName = "brand_agg"; // 品牌聚合名称
        // 对商品分类进行聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        //对品牌进行聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));
        // 3、返回结果
        AggregatedPage<Goods> result = (AggregatedPage<Goods>) goodsRepository.search(queryBuilder.build());
        // 4、解析结果
        //4.1 分页结果解析
        long total = result.getTotalElements();
        Integer totalPage = result.getTotalPages();
        //4.2 聚合结果解析
        Aggregations aggs = result.getAggregations();
        //分类结果解析
        List<Category> categories = parseCategoryAgg(aggs.get(categoryAggName));
        //品牌结果解析
        List<Brand> brands = parseBrandAgg(aggs.get(brandAggName));
        //5.完成规格参数聚合
        List<Map<String, Object>> specs = null;
        //分类存在并且唯一时，才做规格参数聚合
        if(categories != null && categories.size() == 1){
            specs = buildSpecificationAgg(categories.get(0).getId(),baseQuery);
        }
        return new SearchResult(total, totalPage, result.getContent(),categories,brands,specs);
    }

    private QueryBuilder buildBasicQueryWithFilter(SearchRequest request) {
        //创建布尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //查询条件
        boolQueryBuilder.must(QueryBuilders.matchQuery("all",request.getKey()));
        //过滤条件
        Map<String, String> map = request.getFilter();
        for (Map.Entry<String,String> entry : map.entrySet()){
            String key = entry.getKey();
            String value =entry.getValue();
            // 商品分类和品牌要特殊处理
            if (key != "cid3" && key != "brandId") {
                key = "specs." + key + ".keyword";
            }
            boolQueryBuilder.filter(QueryBuilders.termQuery(key,value));
        }
        return boolQueryBuilder;
    }

    /**
     * 规格参数聚合
     * @param id
     * @param baseQuery
     * @return
     */
    private List<Map<String,Object>> buildSpecificationAgg(Long cid, QueryBuilder baseQuery) {
        try{
            List<Map<String,Object>> specs =new ArrayList<>();
            //1.根据分类查询可搜索过滤(需要聚合)的规格参数
            List<SpecParam> specParams = specificationClient.querySpecParamList(null, cid, true);
            //2.聚合
            NativeSearchQueryBuilder queryBuilder =new NativeSearchQueryBuilder();
            //2.1.带上查询条件,在查询的结果上进行聚合
            queryBuilder.withQuery(baseQuery);
            //2.2.聚合
            for (SpecParam param : specParams) {
                String name = param.getName();
                queryBuilder.addAggregation(AggregationBuilders.terms(name).field("specs." + name + ".keyword"));
            }
            //3.获取结果
               AggregatedPage<Goods> result = elasticsearchTemplate.queryForPage(queryBuilder.build(), Goods.class);
            //AggregatedPage<Goods> result = (AggregatedPage<Goods>) goodsRepository.search(queryBuilder.build());
            //解析结果
            Aggregations aggs = result.getAggregations();
            for (SpecParam specParam : specParams) {
                //规格参数名称
                String name = specParam.getName();
                StringTerms terms = (StringTerms) aggs.get(name);
                //规格参数待选项
                List<String> options = terms.getBuckets().stream().
                        map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
                //填充map
                Map<String,Object> map =new HashMap<>();
                map.put("k",name);
                map.put("options",options);
                specs.add(map);
            }
            return specs;
        }catch (Exception e){
            log.error("规格聚合出现异常：", e);
            return null;
        }
    }

    /**
     * 品牌聚合结果
     * @param aggregation
     * @return
     */
    private List<Brand> parseBrandAgg(Aggregation aggregation) {
        try {
            LongTerms brandAgg = (LongTerms) aggregation;
            List<Long> bids = brandAgg.getBuckets().stream().map(bucket -> bucket.getKeyAsNumber().longValue())
                    .collect(Collectors.toList());
            List<Brand> brands = brandClient.queryBrandsByIds(bids);
            return brands;
        }catch (Exception e){
            log.error("品牌聚合出现异常：", e);
            return null;
        }
    }

    /**
     * 解析分类聚合结果
     * @param aggregation
     * @return
     */
    private List<Category> parseCategoryAgg(Aggregation aggregation) {
        try{
            LongTerms categoryAgg = (LongTerms) aggregation;
            List<Long> cids = categoryAgg.getBuckets().stream().map(bucket -> bucket.getKeyAsNumber().longValue())
                    .collect(Collectors.toList());
            List<Category> categories = categoryClient.queryCategoryListByIds(cids);
            return  categories;
        }catch (Exception e){
            log.error("分类聚合出现异常：", e);
            return null;
        }
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }
}
