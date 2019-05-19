package com.huawei.item.service;

import com.huawei.common.vo.PageResult;
import com.huawei.item.param.SpuParam;
import com.huawei.item.pojo.Sku;
import com.huawei.item.pojo.SpuDetail;
import com.huawei.item.vo.SpuVO;

import java.util.List;

/**
 * @program: huaweishop
 * @description: 商品的业务逻辑层
 * @author: cuibeijie
 * @create: 2019-05-08 20:59
 */
public interface GoodsSerivce {
    //分页查询商品
    PageResult<SpuVO> querySpuByPageAndSort(Integer page, Integer rows, Boolean saleable,String key);
    //新增商品
    void saveGoods(SpuParam spuParam);
   //spu详情
    SpuDetail querySpuDetailById(Long id);
   //sku集合
    List<Sku> querySkuBySpuId(Long id);
   //修改商品
    void updateGoods(SpuParam spuParam);
}
