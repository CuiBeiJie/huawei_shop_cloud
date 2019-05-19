package com.huawei.search.client;

import com.huawei.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @program: huaweishop
 * @description: 规格参数的client
 * @author: cuibeijie
 * @create: 2019-05-19 15:09
 */
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
