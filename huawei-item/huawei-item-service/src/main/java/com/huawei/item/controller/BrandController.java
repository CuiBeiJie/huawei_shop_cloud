package com.huawei.item.controller;

import com.huawei.common.parameter.BrandQueryByPageParameter;
import com.huawei.common.vo.PageResult;
import com.huawei.item.pojo.Brand;
import com.huawei.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: cuibeijie
 * @Date: 2019/5/3 20:01
 * @Description:品牌管理控制层
 */
@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    /**
     * 分页查询品牌
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                              @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                                              @RequestParam(value = "sortBy", required = false) String sortBy,
                                                              @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
                                                              @RequestParam(value = "key", required = false) String key){
        BrandQueryByPageParameter brandQueryByPageParameter=new BrandQueryByPageParameter(page,rows,sortBy,desc,key);
        PageResult<Brand> result = brandService.queryBrandByPage(brandQueryByPageParameter);
        if(result == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 品牌新增
     * @param brand
     * @param categories
     * @return
     */
    @PostMapping
    public ResponseEntity<Void>  saveBrand(Brand brand, @RequestParam("cids") List<Long> categories){
        brandService.saveBrand(brand, categories);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 品牌修改
     * @param brand
     * @param categories
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateBrand(Brand brand,@RequestParam("cids") List<Long> categories){
        brandService.updateBrand(brand,categories);
        return  ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * 删除tb_brand中的数据,单个删除、多个删除二合一
     * @param bid
     * @return 因为delete方式没有权限暂时用get方式
     */
    @DeleteMapping("{bid}")
    public ResponseEntity<Void> deleteBrand(@PathVariable("bid") String bid){
        String separator="-";
        if(bid.contains(separator)){
            String[] ids=bid.split(separator);
            for (String id:ids){
               brandService.deleteBrand(Long.parseLong(id));
            }
        }
        else {
            brandService.deleteBrand(Long.parseLong(bid));
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 根据cid查询品牌信息
     * @param cid
     * @return
     */
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable("cid") Long cid){
        List<Brand> brandList = brandService.queryBrandByCatrgory(cid);
        return ResponseEntity.ok(brandList);
   }
}
