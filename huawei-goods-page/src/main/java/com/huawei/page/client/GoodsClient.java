package com.huawei.page.client;

import com.huawei.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @program: huaweishop
 * @description: 商品client
 * @author: cuibeijie
 * @create: 2019-05-18 22:10
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
