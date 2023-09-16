package com.sky.service.impl;


import com.sky.context.BaseContext;
import com.sky.dto.OrdersDTO;
import com.sky.entity.*;
import com.sky.mapper.*;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    private final OrderDetailMapper orderDetailMapper;

    private final AddressBookMapper addressBookServiceMapper;

    private final UserMapper userMapper;

    private final ShoppingCartMapper shoppingCartMapper;


    /**
     * 用户下单
     *
     * @param ordersDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderSubmitVO submit(OrdersDTO ordersDTO) {
        //添加数据到下单列表页
        Orders order = new Orders();
        //补全数据
        BeanUtils.copyProperties(ordersDTO, order);
        //number订单号
        String number = UUID.randomUUID().toString().replace("-", "");
        order.setNumber(number);
        //status 默认为1 待付款
        order.setStatus(Orders.PENDING_PAYMENT);
        //user_id
        Long userId = BaseContext.get();
        order.setUserId(userId);
        //pay_status
        order.setPayStatus(Orders.UN_PAID);
        //order_time 下单时间
        order.setOrderTime(LocalDateTime.now());
        //手机号 地址信息需要查询
        AddressBook addressBook = addressBookServiceMapper.findById(order.getAddressBookId());
        order.setPhone(addressBook.getPhone());
        //address
        order.setAddress(addressBook.getProvinceName() + addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail());
        //user_name 查找用户表
        User user = userMapper.findById(order.getUserId());
        order.setUserName(user.getName());
        //consignee 收货人
        order.setConsignee(addressBook.getConsignee());
        //保存到Orders数据库
        orderMapper.add(order);
        log.info("1111111111{}", order.getId());
        //补充order_detail数据
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.findByUserIdAll(userId);
        //遍历购物车数据补充数据
        List<OrderDetail> orderDetails = new ArrayList<>();
        orderDetails = shoppingCartList.stream().map(
                shoppingCart -> {
                    OrderDetail orderDetail = new OrderDetail();
                    BeanUtils.copyProperties(shoppingCart, orderDetail, "id");
                    orderDetail.setOrderId(order.getId());
                    return orderDetail;
                }
        ).collect(Collectors.toList());
        //插入数据
        orderDetailMapper.add(orderDetails);
        return OrderSubmitVO.builder()
                .orderTime(LocalDateTime.now())
                .orderAmount(ordersDTO.getAmount())
                .orderNumber(number)
                .build();
    }
}
