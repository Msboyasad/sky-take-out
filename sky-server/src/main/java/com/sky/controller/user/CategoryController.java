package com.sky.controller.user;


import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@Api(tags = "C端分类相关接口")
@Slf4j
@RestController("userCategoryController")
@RequestMapping("/user/category")
public class CategoryController {


    @Resource
    private CategoryService categoryService;



    @GetMapping("/list")
    @ApiOperation("查询分类套餐接口")
    public Result list(Long type) {
        List<Category> categoryList = categoryService.listType(type);
        return Result.success(categoryList);
    }
}
