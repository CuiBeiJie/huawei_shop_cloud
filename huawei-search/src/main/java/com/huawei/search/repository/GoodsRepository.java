package com.huawei.search.repository;

import com.huawei.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @program: huaweishop
 * @description: 商品repository
 * @author: cuibeijie
 * @create: 2019-05-19 16:01
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
