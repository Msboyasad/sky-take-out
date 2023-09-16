package com.sky.mapper;


import com.github.pagehelper.Page;
import com.sky.annotation.Autofill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {


    @Select("select count(*) from sky_take_out.dish where category_id = #{id}")
    Integer count(Long id);

    @Autofill(OperationType.INSERT)
    void sava(Dish dish);

    /**
     * 分页查询菜品信息 对表进行拆分 两条sql进行查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    Page<Dish> findCondition(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 分页查询菜品信息一条sql进行多表查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> findConditionByCategory(DishPageQueryDTO dishPageQueryDTO);


    /**
     * 根据ID查询状态为起售
     *
     * @param ids
     * @return
     */
    Integer findStatusById(List<Long> ids);

    /**
     * 根据ID批量删除菜品信息
     *
     * @param ids
     */
    void BatchDelete(List<Long> ids);


    /**
     * 根据ID获取菜品信息
     *
     * @param dish
     * @return
     */
    //@Select("select * from sky_take_out.dish where id =#{id}")
    List<DishVO> findByIdOrCategoryId(Dish dish);

    /**
     * 根据ID获取菜品信息和口味信息
     *
     * @param id
     * @return
     */
    DishVO findDishesWithFlavors(Long id);

    @Autofill(OperationType.INSERT)
    void update(Dish dish);

    /**
     * 根据ID查询
     * @param dishId
     * @return
     */
    @Select("select  * from sky_take_out.dish where id = #{dishId}")
    Dish findById(Long  dishId);
}
