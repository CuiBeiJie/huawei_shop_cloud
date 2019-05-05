package com.huawei.common.parameter;

import lombok.Data;

/**
 * @Auther: 25349
 * @Date: 2019/5/3 20:18
 * @Description:品牌分页查询对象
 */
@Data
public class BrandQueryByPageParameter {
    /*
    *   - page：当前页，int
        - rows：每页大小，int
        - sortBy：排序字段，String
        - desc：是否为降序，boolean
        - key：搜索关键词，String
    * */

    private Integer page;
    private Integer rows;
    private String sortBy;
    private Boolean desc;
    private String key;

    public BrandQueryByPageParameter(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        this.page = page;
        this.rows = rows;
        this.sortBy = sortBy;
        this.desc = desc;
        this.key = key;
    }
}
