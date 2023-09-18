package com.sky.controller.user;


import com.sky.context.BaseContext;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderDetailVO;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/order")
@Api(tags = "用户下单")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    /**
     * 用户下单接口
     *
     * @param ordersDTO
     * @return
     */
    @ApiOperation("用户下单接口")
    @PostMapping("/submit")
    public Result submit(@RequestBody OrdersDTO ordersDTO) {
        OrderSubmitVO orderSubmitVO = orderService.submit(ordersDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }


    /**
     * 查询订单详情接口
     *
     * @param id
     * @return
     */
    @ApiOperation("查询订单详情接口")
    @GetMapping("/orderDetail/{id}")
    public Result orderDetail(@PathVariable Long id) {
        log.info("查询订单详情用户为:{}", id);
        OrderDetailVO orderDetailVO = orderService.findOrderDetail(id);
        return Result.success(orderDetailVO);
    }

    /**
     * 历史订单查询接口
     */
    @ApiOperation("历史订单查询接口")
    @GetMapping("/historyOrders")
    public Result historyOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("历史订单查询");
        Long userId = BaseContext.get();
        ordersPageQueryDTO.setUserId(userId);
        PageResult pageResult = orderService.findHistoryOrders(ordersPageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 再来一单接口
     *
     * @param id
     * @return
     */
    @ApiOperation("在来一单接口")
    @PostMapping("/repetition/{id}")
    public Result repetition(@PathVariable Long id) {
        orderService.repetition(id);
        return Result.success();
    }


    /**
     * 取消订单接口
     *
     * @param id
     * @return
     */
    @ApiOperation("取消订单接口")
    @PutMapping("/cancel/{id}")
    public Result cancel(@PathVariable Long id) {
        Orders orders = Orders.builder()
                .id(id)
                .status(Orders.CANCELLED)
                .build();
        orderService.cancel(orders);
        return Result.success();
    }

}
