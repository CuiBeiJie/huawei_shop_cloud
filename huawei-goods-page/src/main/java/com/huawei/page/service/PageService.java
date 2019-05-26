package com.huawei.page.service;

import java.util.Map;

/**
 * @program: huaweishop
 * @description: 详情页面接口层
 * @author: cuibeijie
 * @create: 2019-05-26 17:08
 */
public interface PageService {
    //加载数据
    Map<String, Object> loadModel(Long spuId);
}
