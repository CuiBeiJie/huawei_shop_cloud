package com.huawei.page.service;

/**
 * @program: huaweishop
 * @description: FileService
 * @author: cuibeijie
 * @create: 2019-05-27 21:34
 */
public interface FileService {
    //生成静态文件
    public void createHtml(Long id);
    //判断商品详情是否存在
    boolean exists(Long id);
    //异步生成商品详情静态html
    public void syncCreateHtml(Long id);
}
