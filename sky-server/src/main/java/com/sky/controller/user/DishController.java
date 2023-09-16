package com.sky.controller.user;


import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 根据分类ID查询菜品信息
     *
     * @param dish
     * @return
     */
    @ApiOperation("根据分类ID查询菜品信息")
    @GetMapping("/list")
    public Result list(Dish dish) {
        //创建redis的key
        String key = "dish_" + dish.getCategoryId();
        //去redis数据库获取数据
        ValueOperations opsForValue = redisTemplate.opsForValue();
        List<DishVO> dishVOList = (List<DishVO>) opsForValue.get(key);
        if (dishVOList != null) {
            //判断数据是否为空  不为空的话就返回数据
            return Result.success(dishVOList);
        }
        //为空的就就先去数据查询
        dishVOList = dishService.findCategoryId(dish);
        //然后写入到redis缓存
        opsForValue.set(key, dishVOList);
        return Result.success(dishVOList);
    }
}
