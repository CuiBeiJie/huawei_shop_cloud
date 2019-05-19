package com.huawei.search.service;

import com.huawei.common.vo.PageResult;
import com.huawei.item.pojo.Spu;
import com.huawei.item.vo.SpuVO;
import com.huawei.search.pojo.Goods;
import com.huawei.search.pojo.SearchRequest;

/**
 * @program: huaweishop
 * @description: 搜索接口层
 * @author: cuibeijie
 * @create: 2019-05-19 16:48
 */
public interface SearchService {
    //构建goods
    public Goods buildGoods(SpuVO spu);
    //搜索
    PageResult<Goods> search(SearchRequest request);
}
