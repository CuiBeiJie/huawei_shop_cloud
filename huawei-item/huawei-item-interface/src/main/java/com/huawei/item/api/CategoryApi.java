package com.huawei.item.api;

import com.huawei.item.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @program: huaweishop
 * @description: 商品分类的接口api
 * @author: cuibeijie
 * @create: 2019-05-19 14:55
 */
public interface CategoryApi {
    /**
     * 批量查询商品分类
     * @param ids
     * @return
     */
    @GetMapping("category/list/ids")
    List<Category> queryCategoryListByIds(@RequestParam("ids") List<Long> ids);
}
