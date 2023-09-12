package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

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
        categoryMapper.deleteById(id);
    }

    /**
     * 修改员工状态信息
     *
     * @param category
     */
    @Override
    public void update(Category category) {
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.get());
        categoryMapper.update(category);
    }

    /**
     * 添加分类信息
     *
     * @param category
     */
    @Override
    public void add(Category category) {
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.get());
        category.setUpdateUser(BaseContext.get());
        category.setStatus(1);
        categoryMapper.add(category);
    }
}
