package com.huawei.search.client;

import com.huawei.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @program: huaweishop
 * @description: 品牌client
 * @author: cuibeijie
 * @create: 2019-05-19 15:01
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
