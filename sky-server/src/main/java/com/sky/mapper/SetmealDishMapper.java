package com.sky.mapper;


import com.sky.annotation.Autofill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 统计出菜品信息的套餐有多少
     * @param dishIds
     */

    Integer findByDishId(List<Long> dishIds);

    /**
     * 批量新增菜品和套餐的绑定信息
     * @param setmealDishes
     */
    void batchSave(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐ID删除
     * @param setmeaId
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmeaId}")
    void delete(Long setmeaId);

    void batchDelete(List<Long> ids);
}
