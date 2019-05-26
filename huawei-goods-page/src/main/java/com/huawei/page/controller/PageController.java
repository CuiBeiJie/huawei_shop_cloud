package com.huawei.page.controller;

import com.huawei.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Map;

/**
 * @program: huaweishop
 * @description: 商品详情控制层
 * @author: cuibeijie
 * @create: 2019-05-25 20:55
 */
@Controller
@RequestMapping("item")
public class PageController {
    @Autowired
    private PageService pageService;
    /**
     * 跳转到商品详情页
     * @param model
     * @param spuId
     * @return
     */
    @GetMapping("{id}.html")
    public String toItemPage(Model model, @PathVariable("id") Long spuId){
        // 加载所需的数据
        Map<String, Object> modelMap = pageService.loadModel(spuId);
        System.out.println("modelMap===="+modelMap);
        // 放入模型
        model.addAllAttributes(modelMap);
        return "item";
    }
}
