package com.huawei.item.vo;

import lombok.Data;

import java.util.Date;

/**
 * @program: huaweishop
 * @description: 商品视图对象
 * @author: cuibeijie
 * @create: 2019-05-08 21:56
 */
@Data
public class SpuVO {
    private Long id;
    private Long brandId;
    private Long cid1;// 1级类目
    private Long cid2;// 2级类目
    private Long cid3;// 3级类目
    private String title;// 标题
    private String subTitle;// 子标题
    private Boolean saleable;// 是否上架
    private Date createTime;// 创建时间
    private String cname;//分类路径名称
    private String bname;//品牌名称
}
