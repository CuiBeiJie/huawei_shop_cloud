package com.huawei.page.client;

import com.huawei.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @program: huaweishop
 * @description: 调用商品微服务中的分类的client
 * @author: cuibeijie
 * @create: 2019-05-18 21:49
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
