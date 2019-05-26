package com.huawei.item.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 商品规格组实体类
 */
@Table(name = "tb_spec_group")
@Data
public class SpecGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cid;//分类id

    private String name;//规格组名称

    @Transient
    private List<SpecParam> params; // 该组下的所有规格参数集合

}