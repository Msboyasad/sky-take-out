package com.sky.mapper;


import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 添加口味信息
     * @param flavors
     */
    void batchSave(List<DishFlavor> flavors);

    /**
     * 删除口味信息
     * @param DishIds
     */
    void batchDelete(List<Long> DishIds);

    /**
     * 根据菜品id获取菜品口味信息
     * @param id
     * @return
     */
    @Select("select * from sky_take_out.dish_flavor where dish_id = #{id}")
    List<DishFlavor> finByDishId(Long id);
}
