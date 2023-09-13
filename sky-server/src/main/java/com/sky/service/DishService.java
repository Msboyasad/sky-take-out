package com.sky.service;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import io.swagger.models.auth.In;

import java.util.List;

public interface DishService {
    /**
     * 统计菜品分类有多少条数据
     * @param id
     * @return
     */
    Integer count(Long id);

    /**
     * 插入菜品数据
     * @param dishDTO
     */
    void save(DishDTO dishDTO);

    /**
     * 分页查询菜品信息 对表进行拆分 两条sql进行查询
     * @param dishPageQueryDTO
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 分页查询菜品信息一条sql进行多表查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageMany(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品信息
     * @param ids
     */
    void batchDelete(List<Long> ids);

    /**
     * 根据ID查询菜品信息
     * @param id
     * @return
     */
    DishVO getById(Long id);

    /**
     * 菜品修改
     * @param dishDTO
     */
    void update(DishDTO dishDTO);


    /**
     * 通过分类ID获取对应所有的菜品
     * @param dish
     * @return
     */
    List<Dish> findCategoryId(Dish dish);
}
