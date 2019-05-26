package com.huawei.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program: huaweishop
 * @description: 商品详情控制层
 * @author: cuibeijie
 * @create: 2019-05-25 20:55
 */
@Controller
@RequestMapping("item")
public class PageController {
    /**
     * 跳转到商品详情页
     * @param model
     * @param spuId
     * @return
     */
    @GetMapping("{id}.html")
    public String toItemPage(Model model, @PathVariable("id") Long spuId){
        return "item";
    }
}
