package com.huawei.item.mapper;

import com.huawei.item.pojo.Stock;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @program: huaweishop
 * @description: 库存的数据库层
 * @author: cuibeijie
 * @create: 2019-05-12 22:55
 */
public interface StockMapper extends Mapper<Stock> , InsertListMapper<Stock> {
}
