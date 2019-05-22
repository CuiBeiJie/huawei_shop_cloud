package com.huawei.search.pojo;

import com.huawei.common.vo.PageResult;
import com.huawei.item.pojo.Brand;
import com.huawei.item.pojo.Category;
import lombok.Data;

import java.util.List;

/**
 * @program: huaweishop
 * @description: 搜索结果
 * @author: cuibeijie
 * @create: 2019-05-22 20:21
 */
@Data
public class SearchResult extends PageResult<Goods> {
    private List<Category> categories;

    private List<Brand> brands;

    public SearchResult(Long total, Integer totalPage, List<Goods> items, List<Category> categories, List<Brand> brands) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
    }

    public SearchResult() {

    }
}
