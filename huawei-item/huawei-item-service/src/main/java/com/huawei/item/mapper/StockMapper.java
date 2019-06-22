package com.huawei.item.mapper;

import com.huawei.common.mapper.BaseMapper;
import com.huawei.item.pojo.Stock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


/**
 * @program: huaweishop
 * @description: 库存的数据库层
 * @author: cuibeijie
 * @create: 2019-05-12 22:55
 */
public interface StockMapper extends BaseMapper<Stock,Long> {
    @Update("update tb_stock set stock = stock - #{num} where sku_id = #{skuId} and stock >= #{num}")
    int decreaseStock(@Param("skuId") Long skuId, @Param("num") Integer num);
}
