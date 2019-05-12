package com.huawei.item.service.impl;

import com.huawei.common.enums.ExceptionEnums;
import com.huawei.common.exception.SelfException;
import com.huawei.item.mapper.SpecGroupMapper;
import com.huawei.item.mapper.SpecParmMapper;
import com.huawei.item.pojo.SpecGroup;
import com.huawei.item.pojo.SpecParam;
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
    private SpecGroupMapper specGroupMapper;//商品规格组mapper

    @Autowired
    private SpecParmMapper specParmMapper;//商品规格参数mapper

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

    //更新商品规格组
    public void updateSpecGroup(SpecGroup specGroup) {
        int count = specGroupMapper.updateByPrimaryKeySelective(specGroup);
        if(count != 1){
            throw new SelfException(ExceptionEnums.SPECGROUP_UPDATE_ERROR);
        }
    }

    //查询规格参数
    public List<SpecParam> querySpecParams(Long gid,Long cid,Boolean searching) {
        //组装查询条件
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        List<SpecParam> specParamsList = specParmMapper.select(specParam);
        if(CollectionUtils.isEmpty(specParamsList)){
            throw new SelfException(ExceptionEnums.SPECPARM);
        }
        return specParamsList;
    }

    //新增商品规格参数
    public void saveSpecParm(SpecParam specParam) {
        int conut = specParmMapper.insert(specParam);
        if(conut != 1){
            throw new SelfException(ExceptionEnums.SPECPARM_SAVE_ERROR);
        }
    }

    //更新商品规格参数
    public void updateSpecParam(SpecParam specParam) {
          Integer count;
         //需要校验一下同一个规则参数组内的规格参数名称有没有重复
         count = specParmMapper.validateSpecParam(specParam.getId(),specParam.getCid(),specParam.getGroupId(),specParam.getName());
         if(count != null && count>=1){
             throw  new SelfException(ExceptionEnums.EXISTS_SPECPARM_NAME);
         }
         count = specParmMapper.updateByPrimaryKeySelective(specParam);
         if(count != 1){
             throw  new SelfException(ExceptionEnums.SPECPARM_UPDATE_ERROR);
         }
    }

    //删除商品规格参数
    public void deleteSpecParam(Long id) {
        int count = specParmMapper.deleteByPrimaryKey(id);
        if(count != 1){
            throw new SelfException(ExceptionEnums.SPEPARAM_DELETE_ERROR);
        }
    }

}
