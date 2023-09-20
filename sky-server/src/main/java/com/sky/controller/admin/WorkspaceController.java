package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.annotation.Target;

/**
 * @Description TODO
 * @Author XiaLiu
 * @Date 2023-09-20 20:26
 */


@RestController
@Slf4j
@RequestMapping("/admin/workspace/")
@Api(tags = "工作台相关接口")
public class WorkspaceController {


    @Resource
    private WorkspaceService workspaceService;


    /**
     * 查询今日运营数据接口
     *
     * @return
     */
    @ApiOperation("查询今日运营数据接口")
    @GetMapping("/businessData")
    public Result businessData() {
        log.info("查询今日运营数据");
        BusinessDataVO businessDataVO = workspaceService.businessData();
        return Result.success(businessDataVO);
    }


    /**
     * 查询套餐总览接口
     *
     * @return
     */
    @ApiOperation("查询套餐总览接口")
    @GetMapping("/overviewSetmeals")
    public Result overviewSetmeals() {
        log.info("查询套餐总览");
        SetmealOverViewVO setmealOverViewVO = workspaceService.overviewSetmeals();
        return Result.success(setmealOverViewVO);
    }


    /**
     * 查询菜品总览接口
     *
     * @return
     */
    @ApiOperation("查询菜品总览接口")
    @GetMapping("/overviewDishes")
    public Result overviewDishes() {
        log.info("查询菜品总览");
        DishOverViewVO dishOverViewVO = workspaceService.overviewDishes();
        return Result.success(dishOverViewVO);
    }


    /**
     * 查询订单管理数据接口
     *
     * @return
     */
    @ApiOperation("查询订单管理数据接口")
    @GetMapping("/overviewOrders")
    public Result overviewOrders() {
        log.info("查询订单管理数据");
        OrderOverViewVO orderOverViewVO = workspaceService.overviewOrders();
        return Result.success(orderOverViewVO);
    }
}
