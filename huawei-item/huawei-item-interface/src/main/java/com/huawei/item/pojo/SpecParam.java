package com.huawei.item.pojo;

import lombok.Data;

import javax.persistence.*;

@Table(name = "tb_spec_param")
@Data
public class SpecParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cid;
    @Column(name = "group_id")
    private Long groupId;
    private String name;
    @Column(name = "`numeric`")
    private Boolean numeric;//数据库的关键字，需要转义变成普通字符
    private String unit;
    private Boolean generic;
    private Boolean searching;
    private String segments;
}