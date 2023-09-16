package com.sky.controller.admin;


import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "分类管理相关接口")
@Slf4j
@RestController
@RequestMapping("/admin/category")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询分类管理接口
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @ApiOperation("分页查询分类管理接口")
    @GetMapping("/page")
    public Result page(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageResult pageResult = categoryService.page(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据ID删除分类接口
     *
     * @param id
     * @return
     */
    @ApiOperation("根据ID删除分类接口")
    @DeleteMapping
    public Result deleteById(Long id) {
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * 修改员工状态信息
     *
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("修改分类管理状态接口")
    @PostMapping("/status/{status}")
    public Result status(@PathVariable Integer status, Long id) {
        Category category = Category.builder()
                .status(status)
                .id(id)
                .build();
        categoryService.update(category);
        return Result.success();
    }

    /**
     * 修改分类管理信息接口
     *
     * @param categoryDTO
     * @return
     */
    @ApiOperation("修改分类管理信息接口")
    @PutMapping
    public Result update(@RequestBody CategoryDTO categoryDTO) {
        Category category = Category.builder().build();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryService.update(category);
        return Result.success();
    }

    /**
     * 添加分类信息接口
     *
     * @param categoryDTO
     * @return
     */
    @ApiOperation("添加分类信息接口")
    @PostMapping
    public Result add(@RequestBody CategoryDTO categoryDTO) {
        Category category = Category.builder().build();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryService.add(category);
        return Result.success();
    }

    /**
     * 根据类型获取分类信息接口
     * @param type
     * @return
     */
    @ApiOperation("根据类型获取分类信息接口")
    @GetMapping("/list")
    public Result list(Long type){
        List<Category> categoryList =categoryService.listType(type);
        return Result.success(categoryList);
    }

}
