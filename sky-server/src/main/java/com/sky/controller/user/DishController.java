package com.sky.controller.user;


import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Api(tags = "C端菜品相关接口")
public class DishController {

    @Resource
    private DishService dishService;


    /**
     * 根据分类ID查询菜品信息
     *
     * @param dish
     * @return
     */
    @ApiOperation("根据分类ID查询菜品信息")
    @GetMapping("/list")
    public Result list(Dish dish) {
        List<DishVO> dishVOList = dishService.findCategoryId(dish);
        return Result.success(dishVOList);
    }
}
