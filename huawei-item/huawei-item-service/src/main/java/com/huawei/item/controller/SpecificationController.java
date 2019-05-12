package com.huawei.item.controller;

import com.huawei.common.enums.ExceptionEnums;
import com.huawei.common.exception.SelfException;
import com.huawei.item.pojo.SpecGroup;
import com.huawei.item.pojo.SpecParam;
import com.huawei.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: huaweishop
 * @description: 商品规格控制层
 * @author: cuibeijie
 * @create: 2019-05-05 20:16
 */
@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;
    /**
     * 商品分类查询商品规格组
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroupBycid(@PathVariable("cid") Long cid){
        if(null == cid || cid<0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(specificationService.querySepcGroupBycid(cid));
    }

    /**
     * 新增一个商品规格组
     * @param specGroup
     * @return
     */
    @PostMapping("group")
    public ResponseEntity<Void> saveSpecification(SpecGroup specGroup){
        if(null == specGroup){
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //校验一下商品规格组名称重复性校验
        //先查询出该分类下的所有商品规格组
        List<SpecGroup> specGroupList = specificationService.querySepcGroupBycid(specGroup.getCid());
        //利用流的方式获取商品规格名称集合
        List<String> specGroupNameList = specGroupList.parallelStream().map(e -> e.getName()).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(specGroupNameList) && specGroupNameList.contains(specGroup.getName())){
            throw new SelfException(ExceptionEnums.EXISTS_SPECGROUP_NAME);
        }
        specificationService.saveSpecGropu(specGroup);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 修改商品规格组
     * @param specGroup
     * @return
     */
    @PutMapping("group")
    public ResponseEntity<Void> updateSpecification(SpecGroup specGroup){
        int count = 0;
        if(null == specGroup){
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //重复性校验
        //校验一下商品规格组名称重复性校验

        this.specificationService.updateSpecGroup(specGroup);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 删除商品规格组
     * @param id
     * @return
     */

    @DeleteMapping("group/{id}")
    public ResponseEntity<Void> deleteSpecification(@PathVariable("id") Long id){
        if(null == id){
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //删除商品规格组
        this.specificationService.deleteSpecGroupByID(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 查询规格参数
     * @param gid
     * @return  http://api.huawei.com/api/item/spec/params?cid=76
     */
    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> querySpecParamList(
            @RequestParam(value="gid", required = false) Long gid,
            @RequestParam(value="cid", required = false) Long cid,
            @RequestParam(value="searching", required = false) Boolean searching
            ){
             //入参不能全部为空
             if(null == gid && null == cid && null == searching){
                 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
             }
        return ResponseEntity.ok(specificationService.querySpecParams(gid,cid,searching));
    }

    /**
     * 新增规格参数
     * @param specParam
     * @return
     */
    @PostMapping("param")
    public ResponseEntity<Void> saveSpecParm(SpecParam specParam){
        if(null == specParam){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        specificationService.saveSpecParm(specParam);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 更新规格参数
     * @param specParam
     * @return
     */
    @PutMapping("param")
    public ResponseEntity<Void> updateSpecParm(SpecParam specParam){
        if(null == specParam){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        specificationService.updateSpecParam(specParam);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 删除规格参数
     * @param id
     * @return
     */
    @DeleteMapping("param/{id}")
    public ResponseEntity<Void> deleteSpecParm(@PathVariable("id") Long id){
        if(null == id ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        specificationService.deleteSpecParam(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
