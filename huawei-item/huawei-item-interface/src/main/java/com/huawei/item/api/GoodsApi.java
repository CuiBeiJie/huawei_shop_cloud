package com.huawei.item.api;

import com.huawei.common.vo.PageResult;
import com.huawei.item.param.SpuParam;
import com.huawei.item.pojo.Sku;
import com.huawei.item.pojo.SpuDetail;
import com.huawei.item.vo.SpuVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @program: huaweishop
 * @description: 商品相关接口供其他微服务调用
 * @author: cuibeijie
 * @create: 2019-05-19 14:46
 */
public interface GoodsApi {
    /**
     *查询商品详情
     * @param id 应该是spu_id
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    SpuDetail querySpuDetailById(@PathVariable("id") Long id);

    /**
     * 根据spu查询下面所有sku
     * @param id - 应该是spu的id
     * @return
     */
    @GetMapping("sku/list")
    List<Sku> querySkuBySpuId(@RequestParam("id") Long id);
    /**
     * 分页查询SPU
     * @param page
     * @param rows
     * @param key
     * @return
     */
    @GetMapping("/spu/page")
    PageResult<SpuVO> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key);

    /**
     * 根据spu的id查询spu
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    SpuParam querySpuById(@PathVariable("id") Long id);
}
