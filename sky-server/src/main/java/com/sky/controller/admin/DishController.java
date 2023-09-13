package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品管理相关接口")
public class DishController {


    @Autowired
    private DishService dishService;

    /**
     * 菜品添加接口
     *
     * @param dishDTO
     * @return
     */
    @ApiOperation("菜品添加接口")
    @PostMapping
    public Result add(@RequestBody DishDTO dishDTO) {
        List<DishFlavor> flavors = dishDTO.getFlavors();
        log.info("11111{}", flavors);
        dishService.save(dishDTO);
        return Result.success();
    }

    /**
     * 菜品的分页查询接口
     *
     * @param dishPageQueryDTO
     * @return
     */
    @ApiOperation("菜品的分页查询接口")
    @GetMapping("/page")
    public Result page(DishPageQueryDTO dishPageQueryDTO) {
        //分页查询菜品信息 对表进行拆分 两条sql进行查询
        //PageResult pageResult = dishService.page(dishPageQueryDTO);
        //分页查询菜品信息一条sql进行多表查询
        PageResult pageResult = dishService.pageMany(dishPageQueryDTO);

        return Result.success(pageResult);
    }


    /**
     * 菜品删除接口
     *
     * @param ids
     * @return
     */
    @ApiOperation("菜品删除接口")
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids) {
        dishService.batchDelete(ids);
        return Result.success();
    }

    /**
     * 根据ID获取菜品信息接口
     *
     * @param id
     * @return
     */
    @ApiOperation("根据ID获取菜品信息接口")
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品信息接口
     *
     * @param dishDTO
     * @return
     */
    @ApiOperation("修改菜品信息接口")
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO) {
        dishService.update(dishDTO);
        return Result.success();
    }

    /**
     * 修改菜品状态
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("修改菜品状态接口")
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status, Long id) {
        DishDTO dishDTO = new DishDTO();
        dishDTO.setStatus(status);
        dishDTO.setId(id);
        dishService.update(dishDTO);
        return Result.success();
    }

    /**
     * 通过分类ID获取对应所有的菜品接口
     * @param categoryId
     * @return
     */
    @ApiOperation("通过分类ID获取对应所有的菜品接口")
    @GetMapping("/list")
    public Result findCategoryId(Long categoryId) {
        Dish dish = Dish.builder().categoryId(categoryId).build();
        List<Dish> dishList = dishService.findCategoryId(dish);
        return Result.success(dishList);
    }


}
