package com.huawei.item.service.impl;

import com.huawei.common.enums.ExceptionEnums;
import com.huawei.common.exception.SelfException;
import com.huawei.item.mapper.CategoryMapper;
import com.huawei.item.pojo.Category;
import com.huawei.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Auther: cuibeijie
 * @Date: 2019/5/3 16:36
 * @Description:分类管理的业务实现层
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> queryLast() {
        List<Category> last =this.categoryMapper.selectLast();
        return last;
    }

    @Override
    public List<Category> queryCategoryListByPid(Long pid) {
        Category t=new Category();
        t.setParentId(pid);
        //根据parentId查询分类
        return this.categoryMapper.select(t);//把对象中的非空字段作为查询条件
    }

    /**
     * 根据品牌id查询分类
     * @param bid
     * @return
     */
    @Override
    public List<Category> queryByBrandId(Long bid) {
        return this.categoryMapper.queryByBrandId(bid);
    }

    /**
     * 批量查询分类
     * @param ids
     * @return
     */
    @Override
    public List<Category> queryByIds(List<Long> ids) {
        List<Category> categories = this.categoryMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(categories)) {
            //没有找到返回404
            throw new SelfException(ExceptionEnums.CATEGORY_NOT_FOUND);
        }
        return categories;
    }
}
