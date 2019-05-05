package com.huawei.item.service;

import com.huawei.common.parameter.BrandQueryByPageParameter;
import com.huawei.common.vo.PageResult;
import com.huawei.item.pojo.Brand;

import java.util.List;

public interface BrandService {
    //分页查询品牌接口
    PageResult<Brand> queryBrandByPage(BrandQueryByPageParameter brandQueryByPageParameter);
    //新增品牌
    void saveBrand(Brand brand, List<Long> categories);
    //修改品牌
    void updateBrand(Brand brand, List<Long> categories);
    //删除品牌
    void deleteBrand(long parseLong);
}
