package com.huawei.search.pojo;

import com.huawei.common.vo.PageResult;
import com.huawei.item.pojo.Brand;
import com.huawei.item.pojo.Category;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @program: huaweishop
 * @description: 搜索结果
 * @author: cuibeijie
 * @create: 2019-05-22 20:21
 */
@Data
public class SearchResult extends PageResult<Goods> {
    private List<Category> categories; //分类待选项

    private List<Brand> brands; //品牌待选项

    private List<Map<String, Object>> specs; // 规格参数过滤条件


    public SearchResult(Long total, Integer totalPage, List<Goods> items, List<Category> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }


    public SearchResult() {

    }
}
