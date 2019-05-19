package com.huawei.search.client;

import com.huawei.item.api.CategoryApi;
import com.huawei.item.pojo.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @program: huaweishop
 * @description: 调用商品微服务中的分类的client
 * @author: cuibeijie
 * @create: 2019-05-18 21:49
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
