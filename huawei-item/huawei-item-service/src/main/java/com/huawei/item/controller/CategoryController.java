package com.huawei.item.controller;

import com.huawei.common.enums.ExceptionEnums;
import com.huawei.common.exception.SelfException;
import com.huawei.item.pojo.Category;
import com.huawei.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: cuibeijie
 * @Date: 2019/5/3 16:36
 * @Description:分类管理的控制层
 */
@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    /**
     * 根据父节点查询商品类目
     * @param pid
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<Category>> queryCategoryListByPid(@RequestParam("pid") Long pid){
        if(pid < 0 && pid != -1){
            //参数parentId不能不正确
            throw new SelfException(ExceptionEnums.CATEGORY_PARENTID_ERROR);
        }
        //如果pid的值为-1那么需要获取数据库中最后一条数据
        if (pid == -1){
            List<Category> last = this.categoryService.queryLast();
            return ResponseEntity.ok(last);
        }
        else {
            List<Category> list = this.categoryService.queryCategoryListByPid(pid);
            if (CollectionUtils.isEmpty(list)) {
                //没有找到返回404
                //return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                throw new SelfException(ExceptionEnums.CATEGORY_NOT_FOUND);
            }
            //找到返回200
            return ResponseEntity.ok(list);
        }
    }

    /**
     * 用于修改品牌信息时，商品分类信息的回显
     * @param bid
     * @return
     */
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid") Long bid){
        List<Category> list = this.categoryService.queryByBrandId(bid);
        if(list == null || list.size() < 1){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(list);
    }


}
