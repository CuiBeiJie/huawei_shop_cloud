package com.huawei.search.repository;

import com.huawei.common.vo.PageResult;
import com.huawei.item.vo.SpuVO;
import com.huawei.search.client.GoodsClient;
import com.huawei.search.pojo.Goods;
import com.huawei.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsRepositoryTest {
    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private SearchService searchService;

    @Autowired
    private GoodsClient goodsClient;

    @Test
    public void createIndex(){
        // 创建索引
        this.elasticsearchTemplate.createIndex(Goods.class);
        // 配置映射
        this.elasticsearchTemplate.putMapping(Goods.class);
    }

    @Test
    public void  loadData(){
        int page = 1;
        int rows = 100;
        int size = 0;
        do{
            PageResult<SpuVO> spuVOPageResult = goodsClient.querySpuByPage(page, rows, true, null);

            List<SpuVO> spus = spuVOPageResult.getItems();
            if(CollectionUtils.isEmpty(spus)){
                break;
            }
            //spu转成goods
            List<Goods> goods = spus.stream().map(searchService::buildGoods).collect(Collectors.toList());
            //存入索引库
            goodsRepository.saveAll(goods);
            page++;
            size = spus.size();
        }while (size == 100);

    }

}