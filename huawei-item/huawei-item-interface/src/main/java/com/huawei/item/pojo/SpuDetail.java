package com.huawei.item.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
/**
* @Description:    java类作用描述:商品详情
* @Author:         cuibeijie
* @CreateDate:     2019/5/8 20:51
*/
@Table(name="tb_spu_detail")
@Data
public class SpuDetail {
    @Id
    private Long spuId;// 对应的SPU的id
    private String description;// 商品描述
    private String specialSpec;// 商品特殊规格的名称及可选值模板
    private String genericSpec;// 商品的全局规格属性
    private String packingList;// 包装清单
    private String afterService;// 售后服务
}