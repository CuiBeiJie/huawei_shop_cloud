package com.huawei.item.service.impl;

import com.huawei.common.enums.ExceptionEnums;
import com.huawei.common.exception.SelfException;
import com.huawei.item.mapper.SpecGroupMapper;
import com.huawei.item.pojo.SpecGroup;
import com.huawei.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @program: huaweishop
 * @description: 商品规格实现类
 * @author: cuibeijie
 * @create: 2019-05-05 20:19
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    //商品分类查询出商品规格组
    public List<SpecGroup> querySepcGroupBycid(Long cid) {
        //查询实体的非空字段
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> specGroupList = null;
        try {
            specGroupList = specGroupMapper.select(specGroup);
        } catch (Exception e) {
            throw new SelfException(ExceptionEnums.SPECGROUP_NOT_FOUND);
        }
        return specGroupList;
    }

    //新增商品规格组
    public void saveSpecGropu(SpecGroup specGroup) {
        int conut = specGroupMapper.insert(specGroup);
        if(conut != 1)
        {
            throw new SelfException(ExceptionEnums.SPECGROUP_SAVE_ERROR);
        }
    }

   //删除商品规格组
    public void deleteSpecGroupByID(Long id) {
        int count = specGroupMapper.deleteByPrimaryKey(id);
        if(count!=1){
            throw new SelfException(ExceptionEnums.SPECGROUP_DELETE_ERROR);
        }
    }

}
