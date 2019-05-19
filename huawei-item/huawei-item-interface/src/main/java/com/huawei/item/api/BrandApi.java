package com.huawei.item.api;

import com.huawei.item.pojo.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @program: huaweishop
 * @description: 商品品牌相关的api
 * @author: cuibeijie
 * @create: 2019-05-19 14:58
 */
public interface BrandApi {
    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    @GetMapping("brand/{id}")
    Brand queryBrandByid(@PathVariable("id") Long id);

}
