package com.huawei.item.service;

import com.huawei.common.vo.PageResult;
import com.huawei.item.vo.SpuVO;

/**
 * @program: huaweishop
 * @description: 商品的业务逻辑层
 * @author: cuibeijie
 * @create: 2019-05-08 20:59
 */
public interface GoodsSerivce {
    //分页查询商品
    PageResult<SpuVO> querySpuByPageAndSort(Integer page, Integer rows, Boolean saleable,String key);
}
