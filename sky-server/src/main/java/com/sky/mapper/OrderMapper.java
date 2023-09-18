package com.sky.mapper;


import com.github.pagehelper.Page;
import com.sky.entity.Orders;
import com.sky.vo.OrderDetailVO;
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
     * @param id
     * @return
     */
    Page<OrderDetailVO> findById(Long id);
}
