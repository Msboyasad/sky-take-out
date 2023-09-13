package com.sky.service;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    /**
     * 统计套餐数量
     * @param id
     * @return
     */
    Integer count(Long id);

    /**
     * 添加套餐
     * @param setmealDTO
     */
    void add(SetmealDTO setmealDTO);

    /**
     * 分页查询套餐信息
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);


    /**
     * 修改套餐信息
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);


    /**
     * 根据ID获取套餐信息
     * @param id
     * @return
     */
    SetmealVO findSetmealWithCategory(Integer id);


    /**
     * 删除套餐信息
     * @param ids
     */
    void batchDelete(List<Long> ids);
}
