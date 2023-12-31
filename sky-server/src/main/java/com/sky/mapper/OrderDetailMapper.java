package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * 插入下单详情数据
     * @param orderDetails
     */
    void add(List<OrderDetail> orderDetails);
}
