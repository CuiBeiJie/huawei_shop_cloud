package com.huawei.item.service;

import com.huawei.item.pojo.SpecGroup;

import java.util.List;

/**
 * @program: huaweishop
 * @description: 商品规格接口
 * @author: cuibeijie
 * @create: 2019-05-05 20:17
 */
public interface SpecificationService {
    //商品分类查询商品规格组
    List<SpecGroup> querySepcGroupBycid(Long cid);
    //新增商品规格组
    void saveSpecGropu(SpecGroup specGroup);
    //删除商品规格组
    void deleteSpecGroupByID(Long id);
}
