package com.huawei.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum  ExceptionEnums {
    PRICE_CANNOT_BE_NULL(400,"价格不能为空！"),
    BRAND_NOT_FOUND(404,"品牌没有查到"),
    CATEGORY_NOT_FOUND(404,"商品分类没有查到"),
    SPECGROUP_NOT_FOUND(404,"商品规格组没有查到"),
    GOODS_NOT_FOUND(404,"商品没有查到"),
    SPECPARM(404,"商品规格参数没有查到"),
    CATEGORY_PARENTID_ERROR(400,"参数不能为负数"),
    BRAND_SAVE_ERROR(500,"新增品牌失败"),
    SPECGROUP_SAVE_ERROR(500,"新增商品规格组失败"),
    SPECPARM_SAVE_ERROR(500,"新增商品规格参数失败"),
    BRAND_UPDATE_ERROR(500,"更新品牌失败"),
    SPECGROUP_UPDATE_ERROR(500,"更新商品规格组失败"),
    SPECPARM_UPDATE_ERROR(500,"更新产品规格参数失败"),
    BRAND_DELETE_ERROR(500,"删除品牌失败"),
    SPECGROUP_DELETE_ERROR(500,"删除商品规格组失败"),
    SPEPARAM_DELETE_ERROR(500,"删除商品规格参数失败"),
    INVALID_FILE_TYPE(400,"文件类型不合法"),
    UPLOAD_FILE_ERROR(500,"上传文件失败"),
    EXISTS_SPECGROUP_NAME(500,"商品规格组名称已经存在"),
    EXISTS_SPECPARM_NAME(500,"规格参数组内该规格参数名已经存在"),
    GOODS_SAVE_ERROR(500,"商品新增失败"),
    GOODS_DETAIL_NOT_FOUND(404,"商品详情不存在"),
    GOODS_STOCK_NOT_FOUND(404,"商品库存不存在"),
    GOODS_SKU_NOT_FOUND(404,"商品SKU不存在"),
    GOODS_UPDATE_ERROR(500,"商品更新失败"),
    GOODS_ID_CANNOT_BE_NULL(500,"商品id不能为空")
    ;
    private Integer code;
    private String msg;

}
