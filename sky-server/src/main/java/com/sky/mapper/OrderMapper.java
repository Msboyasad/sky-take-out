package com.sky.mapper;


import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderDetailVO;
import com.sky.vo.OrderStatisticsVO;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     *
     * @param order
     */
    void add(Orders order);

    /**
     * 根据订单号和用户id查询订单
     *
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumberAndUserId(String orderNumber);

    /**
     * 修改订单信息
     *
     * @param orders
     */
    void update(Orders orders);


    /**
     * 查看订单详情
     *
     * @param orders
     * @return
     */
    Page<OrderDetailVO> findById(Orders orders);


    /**
     * 管理端分页查询
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> page(OrdersPageQueryDTO ordersPageQueryDTO);


    /**
     * 各个状态的订单数量统计
     * @return
     */
    @Select("select count(status) from sky_take_out.orders where status =#{status}")
    Integer count(Integer status);
}
