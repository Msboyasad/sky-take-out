package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Employee;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 分页查询分类信息
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        Category category = Category.builder()
                .name(categoryPageQueryDTO.getName())
                .type(categoryPageQueryDTO.getType()).build();
        List<Category> categoryList = categoryMapper.page(category);
        Page<Category> pageCategory = (Page<Category>) categoryList;
        return PageResult.builder()
                .total(pageCategory.getTotal())
                .records(pageCategory.getResult())
                .build();
    }

    /**
     * 根据员工ID删除分类信息
     *
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        Category category = categoryMapper.getById(id);
        Integer status = category.getStatus();
        if(status==StatusConstant.ENABLE){
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }
        Integer dishCount = dishService.count(id);
        Integer setmealCount = setmealService.count(id);
        if(dishCount>0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        if(setmealCount>0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        categoryMapper.deleteById(id);
    }

    /**
     * 修改员工状态信息
     *
     * @param category
     */
    @Override
    public void update(Category category) {
        categoryMapper.update(category);
    }

    /**
     * 添加分类信息
     *
     * @param category
     */
    @Override
    public void add(Category category) {
        category.setStatus(StatusConstant.DISABLE);
        categoryMapper.add(category);
    }

    /**
     * 根据类型获取分类信息
     * @param type
     * @return
     */
    @Override
    public List<Category> listType(Integer type) {
        return categoryMapper.listType(type);
    }
}
