package com.sky.mapper;


import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {
    /**
     * 根据openid进行查找
     * @param openid
     * @return
     */
    @Select("select * from sky_take_out.user where openid = #{openid} ")
    User findByOpenid(String openid);

    /**
     * 插入数据
     * @param user
     */
    @Insert("insert into sky_take_out.user values (null,#{openid},#{name},#{phone},#{sex},#{idNumber},#{avatar},#{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(User user);


    /**
     * 根据Id查询用户信息
     * @param userId
     * @return
     */
    @Select("select  * from sky_take_out.user  where id=#{userId}")
    User findById(Long userId);

    /**
     * 统计新增用户数量
     * @return
     */

    Integer countSum(Map map);
}
