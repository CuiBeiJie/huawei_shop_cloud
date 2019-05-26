package com.huawei.item.service;

import com.huawei.item.pojo.SpecGroup;
import com.huawei.item.pojo.SpecParam;

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
    //更新商品规格组
    void updateSpecGroup(SpecGroup specGroup);
    //根据商品规格组id查询规格参数
    List<SpecParam> querySpecParams(Long gid,Long cid,Boolean searching);
    //新增商品规格参数
    void saveSpecParm(SpecParam specParam);
    //更新商品规格参数
    void updateSpecParam(SpecParam specParam);
    //删除商品规格参数
    void deleteSpecParam(Long id);
    // 查询规格参数组，及组内参数
    List<SpecGroup> querySepcListBycid(Long cid);
}
