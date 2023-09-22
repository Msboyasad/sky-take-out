package com.sky.service;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {

    /**
     * 分页查询分类信息
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult page(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     *根据员工ID删除分类信息
     * @param id
     */
    void deleteById(Long id);

    /**
     * 修改分类信息
     * @param category
     */
    void update(Category category);

    /**
     * 添加分类信息
     * @param category
     */
    void add(Category category);

    /**
     * 根据类型获取分类信息
     * @param type
     * @return
     */
    List<Category> listType(Long type);

    /**
     * 获取所有
     */
    List<Category> list();
}
