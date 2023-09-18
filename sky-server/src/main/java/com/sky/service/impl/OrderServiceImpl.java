package com.sky.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.entity.*;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderDetailVO;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Autowired
    private WeChatPayUtil weChatPayUtil;


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

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.get();
        User user = userMapper.findById(userId);

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询当前用户的订单
        Orders ordersDB = orderMapper.getByNumberAndUserId(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    @Override
    public OrderDetailVO findOrderDetail(Long id) {

        Page<OrderDetailVO> page = orderMapper.findById(Orders.builder().id(id).build());
        List<OrderDetailVO> result = page.getResult();
        OrderDetailVO orderDetailVO = result.get(0);
        return orderDetailVO;
    }

    /**
     * 历史订单查询
     *
     * @return
     */
    @Override
    public PageResult findHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Orders orders = new Orders();
        orders.setUserId(ordersPageQueryDTO.getUserId());
        BeanUtils.copyProperties(ordersPageQueryDTO, orders);
        Page<OrderDetailVO> page = orderMapper.findById(orders);
        log.info("111111{}",page.getResult());
        return PageResult.builder()
                .total(page.getTotal())
                .records(page.getResult())
                .build();
    }


    /**
     * 再来一单
     *
     * @param id
     */
    @Override
    public void repetition(Long id) {
        Orders o = Orders.builder().id(id).build();
        //首先根据id查询订单信息
        List<OrderDetailVO> detailVOList = orderMapper.findById(o).getResult();
        OrderDetailVO orderDetailVO = detailVOList.get(0);
        //添加数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(orderDetailVO, orders);
        orders.setId(null);
        orders.setNumber(UUID.randomUUID().toString().replace("-",""));
        orders.setStatus(Orders.PENDING_PAYMENT);
        //添加下单信息
        orderMapper.add(orders);
        //添加下单详情信息
        List<OrderDetail> orderDetailList = orderDetailVO.getOrderDetailList().stream().map(
                orderDetail -> {
                    orderDetail.setOrderId(orders.getId());
                    return orderDetail;
                }
        ).collect(Collectors.toList());
        orderDetailMapper.add(orderDetailList);

    }


    /**
     * 取消订单
     *
     * @param orders
     */
    @Override
    public void cancel(Orders orders) {
        orderMapper.update(orders);
    }


    /**
     * 管理端分页查询
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(),ordersPageQueryDTO.getPageSize());
        Page<Orders> page = orderMapper.page(ordersPageQueryDTO);
        return PageResult.builder()
                .total(page.getTotal())
                .records(page.getResult())
                .build();
    }


    /**
     * 各个状态的订单数量统计
     * @return
     */
    @Override
    public OrderStatisticsVO statistics() {
       Integer count2 = orderMapper.count(Orders.TO_BE_CONFIRMED);
       Integer count3 = orderMapper.count(Orders.CONFIRMED);
       Integer count4 = orderMapper.count(Orders.DELIVERY_IN_PROGRESS);
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(count2);
        orderStatisticsVO.setConfirmed(count3);
        orderStatisticsVO.setDeliveryInProgress(count4);
        return orderStatisticsVO;
    }
}
