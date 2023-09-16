package com.sky.service;


import com.sky.dto.OrdersDTO;
import com.sky.vo.OrderSubmitVO;

public interface OrderService {

    /**
     * 用户下单
     * @param ordersDTO
     */
    OrderSubmitVO submit(OrdersDTO ordersDTO);
}
