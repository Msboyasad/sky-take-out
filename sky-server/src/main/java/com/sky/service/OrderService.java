package com.sky.service;


import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.vo.OrderDetailVO;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;

import java.util.List;

public interface OrderService {

    /**
     * 用户下单
     * @param ordersDTO
     */
    OrderSubmitVO submit(OrdersDTO ordersDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);


    /**
     * 查询订单详情
     * @param id
     * @return
     */
    OrderDetailVO findOrderDetail(Long id);


    /**
     * 历史订单查询
     * @return
     */
    PageResult findHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO);


    /**
     * 在来一单接口
     * @param id
     */
    void repetition(Long id);

    /**
     * 取消订单
     * @param orders
     */
    void cancel(Orders orders);


    /**
     *管理端分页查询
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);


    /**
     * 各个状态的订单数量统计
     * @return
     */
    OrderStatisticsVO statistics();
}
