package com.huawei.item.param;

import com.huawei.item.pojo.Sku;
import com.huawei.item.pojo.SpuDetail;
import lombok.Data;

import java.util.List;

/**
 * @program: huaweishop
 * @description: Spu入参对象
 * @author: cuibeijie
 * @create: 2019-05-12 22:17
 */
@Data
public class SpuParam {
    private Long id;
    private Long brandId;//品牌id
    private Long cid1;// 1级类目
    private Long cid2;// 2级类目
    private Long cid3;// 3级类目
    private String title;// 标题
    private String subTitle;// 子标题
    private List<Sku> skus; //sku
    private SpuDetail spuDetail;//商品详情
}
