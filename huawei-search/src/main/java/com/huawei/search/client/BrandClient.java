package com.huawei.search.client;

import com.huawei.item.api.BrandApi;
import com.huawei.item.pojo.Brand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @program: huaweishop
 * @description: 品牌client
 * @author: cuibeijie
 * @create: 2019-05-19 15:01
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
    @GetMapping("brand/list")
    List<Brand> queryBrandsByIds(@RequestParam("ids") List<Long> ids);
}
