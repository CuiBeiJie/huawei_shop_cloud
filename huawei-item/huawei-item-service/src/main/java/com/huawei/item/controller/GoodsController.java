package com.huawei.item.controller;

import com.huawei.common.vo.PageResult;
import com.huawei.item.service.GoodsSerivce;
import com.huawei.item.vo.SpuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: huaweishop
 * @description: 商品控制层
 * @author: cuibeijie
 * @create: 2019-05-08 21:08
 */
@RestController
public class GoodsController {

    @Autowired
    private GoodsSerivce goodsSerivce;

    /**
     * 分页查询SPU
     * @param page
     * @param rows
     * @param key
     * @return
     */
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<SpuVO>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key) {
        // 分页查询spu信息
        PageResult<SpuVO> result = goodsSerivce.querySpuByPageAndSort(page, rows,saleable, key);
        if (result == null || result.getItems().size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }
}
