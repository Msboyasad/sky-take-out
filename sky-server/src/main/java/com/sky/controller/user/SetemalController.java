package com.sky.controller.user;


import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController("userSetemalController")
@Api(tags = "C端套餐相关接口")
@RequestMapping("/user/setmeal")
public class SetemalController {


    @Resource
    private SetmealService setmealService;

    /**
     * 根据套餐ID查询下面的菜品接口
     *
     * @param id
     * @return
     */
    @ApiOperation("根据套餐ID查询下面的菜品接口")
    @GetMapping("/dish/{id}")
    public Result setmealByDishId(@PathVariable Long id) {
        SetmealVO setmealVO = setmealService.findSetmealWithCategory(id);
        List<SetmealDish> setmealDishes = setmealVO.getSetmealDishes();
        return Result.success(setmealDishes);
    }

    /**
     * 根据分类ID查询套餐信息
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result setmealByCategory(Long categoryId) {
        List<Setmeal> setmealList = setmealService.findBycategoryId(categoryId);
        return Result.success(setmealList);
    }
}
