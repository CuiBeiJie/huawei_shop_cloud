package com.huawei.cart.pojo;
import lombok.Data;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/17 23:18
 * @Description: 物流地址实体类
 */
@Table(name = "tb_shipping")
@Data
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //物流地址id
    private Long userId; //用户id
    private String name; //用户名称
    private String state; //省份
    private String city; //城市
    private String district; //区
    private String address; //街道地址
    private String zipcode; //邮编
    private String phone; //手机号码
    private String addressDetail; //详细地址
    private String addressAlias; //地址别名
    private Date createTime; //创建时间
    private Date updateTime; //修改时间
    private Boolean isdefault;//是否为默认收货地址
}
