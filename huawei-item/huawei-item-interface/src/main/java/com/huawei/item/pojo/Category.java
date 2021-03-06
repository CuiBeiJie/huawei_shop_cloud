package com.huawei.item.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * @author li
 * @time 2018/8/7
 * @feature: 商品分类对应的实体
 */
@Table(name="tb_category")
@Data
public class Category implements Serializable {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	private String name;
	private Long parentId;
	private Boolean isParent;
	/**
	 * 排序指数，越小越靠前
	 */
	private Integer sort;


}