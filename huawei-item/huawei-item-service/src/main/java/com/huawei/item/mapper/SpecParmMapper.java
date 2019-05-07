package com.huawei.item.mapper;

import com.huawei.item.pojo.SpecParam;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 * @program: huaweishop
 * @description: 规格参数数据层
 * @author: cuibeijie
 * @create: 2019-05-07 20:55
 */
public interface SpecParmMapper extends Mapper<SpecParam> {
    //校验同一个规格组内的商品规格参数名称是否重复
    @Select("select * from tb_spec_param where id != #{id} and cid = #{cid} and group_id = #{groupId} and name = #{name} ")
    Integer validateSpecParam(Long id, Long cid, Long groupId,String name);
}
