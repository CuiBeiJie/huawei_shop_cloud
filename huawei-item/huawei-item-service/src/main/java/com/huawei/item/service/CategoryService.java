package com.huawei.item.service;

import com.huawei.item.pojo.Category;

import java.util.List;

/**
 * @Auther: cuibeijie
 * @Date: 2019/5/3 16:36
 * @Description:分类管理的业务层
 */
public interface CategoryService {
    //查询数据库表中的最后一项
    List<Category> queryLast();

    //根据parentid查询分类列表
    List<Category> queryCategoryListByPid(Long pid);

    //根据品牌id查询分类信息
    List<Category> queryByBrandId(Long bid);
}
