package com.sky.controller.admin;


import com.sky.constant.RedisKeyConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "店铺相关接口")
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 设置店铺营业状态接口
     * @param status
     * @return
     */
    @ApiOperation("设置店铺营业状态接口")
    @PutMapping("{status}")
    public Result putStatus(@PathVariable Integer status){
        ValueOperations opsForValue = redisTemplate.opsForValue();
        opsForValue.set(RedisKeyConstant.SHOP_STATUS,status);
        return Result.success();
    }

    /**
     * 获取店铺状态信息接口
     * @return
     */
    @ApiOperation("获取店铺状态信息接口")
    @GetMapping("/status")
    public Result getStatus(){
        ValueOperations opsForValue = redisTemplate.opsForValue();
        Integer status = (Integer) opsForValue.get(RedisKeyConstant.SHOP_STATUS);
        if(status == null){
            status=0;
        }
        log.info("店铺营业状态为{}",status==1 ? "营业中" : "打样中");
        return Result.success(status);
    }
}
