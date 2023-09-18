package com.sky.controller.admin;


import com.sky.context.BaseContext;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderDetailVO;
import com.sky.vo.OrderStatisticsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Api(tags = "管理端订单相关接口")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;


    /**
     * 订单分页查询接口
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @ApiOperation("订单分页查询接口")
    @GetMapping("/conditionSearch")
    public Result conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        //Long userId = BaseContext.get();
        //ordersPageQueryDTO.setUserId(userId);
        PageResult page = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(page);

    }


    /**
     * 查看详情订单接口
     *
     * @param id
     * @return
     */
    @ApiOperation("查看详情订单接口")
    @GetMapping("/details/{id}")
    public Result details(@PathVariable Long id) {
        OrderDetailVO orderDetail = orderService.findOrderDetail(id);
        return Result.success(orderDetail);
    }


    /**
     * 取消订单接口
     *
     * @param orders
     * @return
     */
    @ApiOperation("取消订单接口")
    @PutMapping("/cancel")
    public Result cancel(@RequestBody Orders orders) {
        orders.setStatus(Orders.CANCELLED);
        orderService.cancel(orders);
        return Result.success();
    }


    /**
     * 接单接口
     *
     * @param orders
     * @return
     */
    @ApiOperation("接单接口")
    @PutMapping("/confirm")
    public Result confirm(@RequestBody Orders orders) {
        orders.setStatus(Orders.CONFIRMED);
        return Result.success();
    }


    /**
     * 拒单接口
     *
     * @param orders
     * @return
     */
    @ApiOperation("拒单接口")
    @PutMapping("/rejection")
    public Result rejection(@RequestBody Orders orders) {
        orders.setStatus(Orders.CANCELLED);
        orderService.cancel(orders);
        return Result.success();
    }

    /**
     * 派送订单接口
     *
     * @param id
     * @return
     */
    @ApiOperation("派送订单接口")
    @PutMapping("/delivery/{id}")
    public Result delivery(@PathVariable Long id) {
        Orders orders = Orders.builder()
                .id(id)
                .status(Orders.DELIVERY_IN_PROGRESS)
                .build();
        orderService.cancel(orders);
        return Result.success();
    }

    /**
     * 完成订单接口
     *
     * @param id
     * @return
     */
    @ApiOperation("完成订单接口")
    @PutMapping("/complete/{id}")
    public Result complete(@PathVariable Long id) {
        Orders orders = Orders.builder().id(id).build();
        orders.setStatus(Orders.COMPLETED);
        orderService.cancel(orders);
        return Result.success();
    }

    /**
     * 各个状态的订单数量统计接口
     * @return
     */
    @ApiOperation("各个状态的订单数量统计接口")
    @GetMapping("/statistics")
    public Result statistics(){
        OrderStatisticsVO orderStatisticsVO =orderService.statistics();
        return Result.success(orderStatisticsVO);
    }

}
