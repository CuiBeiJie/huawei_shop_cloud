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
    @Select("select * from tb_spec_param where id != #{id} and cid = #{cid} and group_id = #{groupId} and name = #{name} ")
    Integer validateSpecParam(Long id, Long cid, Long groupId,String name);
}
