package com.huawei.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huawei.common.enums.ExceptionEnums;
import com.huawei.common.exception.SelfException;
import com.huawei.common.vo.PageResult;
import com.huawei.item.mapper.SpuDetailMapper;
import com.huawei.item.mapper.SpuMapper;
import com.huawei.item.pojo.Brand;
import com.huawei.item.pojo.Category;
import com.huawei.item.pojo.Spu;
import com.huawei.item.service.BrandService;
import com.huawei.item.service.CategoryService;
import com.huawei.item.service.GoodsSerivce;
import com.huawei.item.vo.SpuVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: huaweishop
 * @description: 商品的业务逻辑实现层
 * @author: cuibeijie
 * @create: 2019-05-08 20:59
 */
@Service
public class GoodsServiceImpl implements GoodsSerivce {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
// Preparing: SELECT id,brand_id,cid1,cid2,cid3,title,sub_title,saleable,valid,create_time,last_update_time FROM tb_spu
// WHERE ( title like ? or saleable = ? )
// order by last_update_time DESC LIMIT ?
   //分页查询商品
    public PageResult<SpuVO> querySpuByPageAndSort(Integer page, Integer rows,Boolean saleable, String key) {
        // 1、查询SPU
        // 分页,最多允许查100条
        PageHelper.startPage(page, Math.min(rows, 100));
        //过滤
        Example example = new Example(Spu.class);
        //搜索字段过滤
        Example.Criteria criteria = example.createCriteria();
        //按照名称标题模糊查询
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("title", "%" + key + "%");
        }
        //按照上下架过滤
        if(null != saleable){
            criteria.andEqualTo("saleable",saleable);
        }
        //默认排序
        example.setOrderByClause("last_update_time DESC");
        //查询
        List<Spu> spuList = spuMapper.selectByExample(example);
        //判断是否为空
        if(CollectionUtils.isEmpty(spuList)){
            throw  new SelfException(ExceptionEnums.GOODS_NOT_FOUND);
        }
        //解析分页结果
        PageInfo<Spu> spuInfo = new PageInfo<>(spuList);
        //SpuVO对象填充分类和品牌名称
        List<SpuVO> spuvoList = getCategoryAndBrandName(spuList);

        return new PageResult<SpuVO>(spuInfo.getTotal(),spuvoList);
    }

    //SpuVO对象填充分类和品牌名称
    private List<SpuVO> getCategoryAndBrandName(List<Spu> spuList) {
        //利用lanmbda表达式
        List<SpuVO> spuVOList = spuList.stream().map(spu -> {
            SpuVO spuVO = new SpuVO();
            //属性拷贝
            BeanUtils.copyProperties(spu, spuVO);
            //处理分类名称
            List<Category> categories = categoryService.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            //1.利用流的方式得到分类名称的集合
            List<String> categoryNamelist = categories.stream().map(Category::getName).collect(Collectors.toList());
            //2.拼接分类名称
            String joinName = StringUtils.join(categoryNamelist, "/");
            //填充分类名称
            spuVO.setCname(joinName);
            //处理品牌名称
            spuVO.setBname(brandService.queryBrandById(spu.getBrandId()).getName());
            return spuVO;
        }).collect(Collectors.toList());
        return spuVOList;
    }
}
