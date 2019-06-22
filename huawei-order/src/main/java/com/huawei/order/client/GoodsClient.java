package com.huawei.order.client;

import com.huawei.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/21 23:28
 * @Description:
 */
@FeignClient(value = "item-service")
public interface GoodsClient extends GoodsApi {
}
