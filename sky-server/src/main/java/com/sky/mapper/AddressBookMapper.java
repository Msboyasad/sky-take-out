package com.sky.mapper;


import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    /**
     * 根据用户ID查询收获地址的条数
     *
     * @param userId
     * @return
     */
    @Select("select count(1) from address_book where user_id=#{userId}")
    Integer findUserId(Long userId);


    /**
     * 添加地址
     *
     * @param addressBook
     */
    @Insert("insert into address_book values (null,#{userId},#{consignee},#{sex},#{phone},#{provinceCode}," +
            "#{provinceName},#{cityCode},#{cityName}," +
            "#{districtCode},#{districtName},#{detail},#{label},#{isDefault})")
    void add(AddressBook addressBook);


    /**
     * 查询当前登录用户的所有地址信息
     * @return
     */
    @Select("select * from address_book where user_id = #{userId}")
    List<AddressBook> findAll(Long userId);


    /**
     * 查询默认地址
     * @param aLong
     * @return
     */
    @Select("select * from address_book where user_id = #{userId} and is_default=1")
    AddressBook findDefault(Long aLong);


    /**
     * 根据id修改地址
     * @param addressBook
     */
    void update(AddressBook addressBook);

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @Select("select * from address_book where id =#{id}")
    AddressBook findById(Long id);

    /**
     * 根据id删除地址
     * @param id
     */
    @Delete("delete from address_book where id =#{id} ")
    void delete(Long id);
}
