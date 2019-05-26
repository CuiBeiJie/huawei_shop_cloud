package com.huawei.page.service.impl;

import com.huawei.item.param.SpuParam;
import com.huawei.item.pojo.*;
import com.huawei.page.client.BrandClient;
import com.huawei.page.client.CategoryClient;
import com.huawei.page.client.GoodsClient;
import com.huawei.page.client.SpecificationClient;
import com.huawei.page.service.PageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: huaweishop
 * @description: 页面接口实现层
 * @author: cuibeijie
 * @create: 2019-05-26 17:10
 */
@Slf4j
@Service
public class PageServiceImpl implements PageService {
    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;
    /**
     * 加载详情页所需的数据
     * @param spuId
     * @return
     */
    public Map<String, Object> loadModel(Long spuId) {
        try{
            Map<String, Object> model = new HashMap<>();
            //查询spu
            SpuParam spuParam = goodsClient.querySpuById(spuId);
            //查询skus
            List<Sku> skus = spuParam.getSkus();
            //查询spu详情
            SpuDetail spuDetail = spuParam.getSpuDetail();
            //查询brand
            Brand brand = brandClient.queryBrandByid(spuParam.getBrandId());
            //查询商品分类
            List<Category> categories = categoryClient.queryCategoryListByIds(
                    Arrays.asList(spuParam.getCid1(), spuParam.getCid2(), spuParam.getCid3()));
            //查询规格参数
            List<SpecGroup> specs = specificationClient.querySpecsByCid(spuParam.getCid3());
            model.put("brand", brand);
            model.put("categories", categories);
            model.put("spu", spuParam);
            model.put("skus", skus);
            model.put("detail", spuDetail);
            model.put("specs", specs);
            return model;

        }catch (Exception e){
            log.error("加载商品数据出错,spuId:{}", spuId, e);
        }
        return null;
    }
}
