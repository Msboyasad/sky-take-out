package com.sky.controller.user;


import com.sky.constant.RedisKeyConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户端店铺管理接口
 */
@Api(tags = "用户端店铺管理接口")
@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
public class ShopController {

    @Resource
    private RedisTemplate redisTemplate;


    /**
     * 用户端店铺获取状态接口
     * @return
     */
    @ApiOperation("用户端店铺获取状态接口")
    @GetMapping("/status")
    public Result getStatus(){
        ValueOperations opsForValue = redisTemplate.opsForValue();
        Integer status = (Integer) opsForValue.get(RedisKeyConstant.SHOP_STATUS);
        if(status == null){
            status = 0;
        }
        log.info("店铺营业状态为{}",status==1 ? "营业中" : "打样中");
        return Result.success(status);
    }
}
