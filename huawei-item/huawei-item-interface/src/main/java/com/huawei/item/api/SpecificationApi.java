package com.huawei.item.api;

import com.huawei.item.pojo.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @program: huaweishop
 * @description: 商品规格相关接口api
 * @author: cuibeijie
 * @create: 2019-05-19 15:04
 */
public interface SpecificationApi {
    /**
     * 查询规格参数
     * @param gid
     * @return  http://api.huawei.com/api/item/spec/params?cid=76
     */
    @GetMapping("spec/params")
   List<SpecParam> querySpecParamList(
            @RequestParam(value="gid", required = false) Long gid,
            @RequestParam(value="cid", required = false) Long cid,
            @RequestParam(value="searching", required = false) Boolean searching
    );
}
