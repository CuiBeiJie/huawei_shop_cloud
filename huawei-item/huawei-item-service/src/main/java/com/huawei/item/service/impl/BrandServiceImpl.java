package com.huawei.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huawei.common.enums.ExceptionEnums;
import com.huawei.common.exception.SelfException;
import com.huawei.common.parameter.BrandQueryByPageParameter;
import com.huawei.common.vo.PageResult;
import com.huawei.item.mapper.BrandMapper;
import com.huawei.item.pojo.Brand;
import com.huawei.item.service.BrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Auther: cuibeijie
 * @Date: 2019/5/3 19:58
 * @Description:品牌管理业务实现层
 */
@Service
public class BrandServiceImpl implements BrandService {

     @Autowired
    private BrandMapper brandMapper;

    //分页查询品牌接口实现
    public PageResult<Brand> queryBrandByPage(BrandQueryByPageParameter brandQueryByPageParameter) {
        /**
         * 1.分页(利用mybatis拦截器自动在查询sql拼上分页信息)
         */
        PageHelper.startPage(brandQueryByPageParameter.getPage(),brandQueryByPageParameter.getRows());

        /**
         *  2.排序
         */
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(brandQueryByPageParameter.getSortBy())){
            example.setOrderByClause(brandQueryByPageParameter.getSortBy()+(brandQueryByPageParameter.getDesc()? " DESC":" ASC"));
        }
        /**
         * 3.查询
         */
        if(StringUtils.isNotBlank(brandQueryByPageParameter.getKey())) {
            example.createCriteria().orLike("name", "%"+brandQueryByPageParameter.getKey()+"%").orEqualTo("letter", brandQueryByPageParameter.getKey().toUpperCase());
        }
        List<Brand> list=this.brandMapper.selectByExample(example);

        if(CollectionUtils.isEmpty(list)){
            throw new SelfException(ExceptionEnums.BRAND_NOT_FOUND);
        }

        /**
         * 4.创建PageInfo
         */
        PageInfo<Brand> pageInfo = new PageInfo<>(list);
        /**
         * 5.返回分页结果
         */
        return new PageResult<Brand>(pageInfo.getTotal(),pageInfo.getList());
    }

    //新增品牌接口实现
    @Transactional(rollbackFor = Exception.class)
    public void saveBrand(Brand brand, List<Long> categories) {
        // 新增品牌信息
        brand.setId(null);
        int count = brandMapper.insertSelective(brand);
        if(count != 1){
            throw new  SelfException(ExceptionEnums.BRAND_SAVE_ERROR);
        }
        // 新增品牌和分类中间表
        for (Long cid : categories) {
            count = this.brandMapper.insertCategoryBrand(cid, brand.getId());
            if(count != 1){
                throw new  SelfException(ExceptionEnums.BRAND_SAVE_ERROR);
            }
        }
    }

    /**
     * 品牌更新
     * @param brand
     * @param categories
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateBrand(Brand brand,List<Long> categories) {
        // 修改品牌信息
        int count = brandMapper.updateByPrimaryKeySelective(brand);
        if(count != 1){
            throw new SelfException(ExceptionEnums.BRAND_UPDATE_ERROR);
        }
        //先删除品牌与分类的中间表
        try {
            brandMapper.deleteByBrandIdInCategoryBrand(brand.getId());
        } catch (Exception e) {
            throw new SelfException(ExceptionEnums.BRAND_UPDATE_ERROR);
        }
        //维护品牌和分类中间表
        for (Long cid : categories) {
            count = brandMapper.insertCategoryBrand(cid, brand.getId());
            if(count != 1){
                throw new SelfException(ExceptionEnums.BRAND_UPDATE_ERROR);
            }
        }
    }

    /**
     * 品牌删除
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteBrand(long id) {
        //删除品牌信息
         int count = brandMapper.deleteByPrimaryKey(id);
         if(count != 1){
             throw new SelfException(ExceptionEnums.BRAND_DELETE_ERROR);
         }
        //维护中间表
        try {
            brandMapper.deleteByBrandIdInCategoryBrand(id);
        } catch (Exception e) {
            throw new SelfException(ExceptionEnums.BRAND_DELETE_ERROR);
        }
    }

    /**
     * 根据id查询品牌
     * @param brandId
     * @return
     */
    @Override
    public Brand queryBrandById(Long brandId) {

        Brand brand = brandMapper.selectByPrimaryKey(brandId);
        if(brand == null){
            throw new SelfException(ExceptionEnums.BRAND_NOT_FOUND);
        }
        return brand;
    }
}
