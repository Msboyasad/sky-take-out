package com.sky.mapper;


import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderDetailVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.SalesTop10ReportVO;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
     *
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> page(OrdersPageQueryDTO ordersPageQueryDTO);


    /**
     * 各个状态的订单数量统计
     *
     * @return
     */
    @Select("select count(status) from sky_take_out.orders where status =#{status}")
    Integer count(Integer status);

    /**
     * 查询订单未支付 超过15分钟 没付款的订单
     *
     * @param status
     * @param currentTime
     * @return
     */
    @Select("select * from sky_take_out.orders where status = #{status} and order_time <= #{currentTime}")
    List<Orders> findByTimeoutTimer(Integer status, LocalDateTime currentTime);

    /**
     * 根据ID批量修改订单超时数据
     *
     * @param ids
     */
    void updateByTimeoutTimerIds(List<Long> ids, Integer status, String cancelReason, LocalDateTime cancelTime);


    /**
     * 查询派送中数据
     *
     * @param status
     * @param cancelTime
     * @return
     */
    @Select("select * from sky_take_out.orders where status = #{status} and order_time <= #{cancelTime}")
    List<Orders> findByDelivered(Integer status, LocalDateTime cancelTime);

    /**
     * 根据ID批量修改订单已完成数据
     *
     * @param ids
     * @param status
     * @param now
     */
    void updateByDeliveredIds(List<Long> ids, Integer status, LocalDateTime now);


    /**
     * 根据条件对每日营业额进行求和
     *
     * @param map
     * @return
     */
    Double countSum(Map<String, Object> map);

    /**
     * 计算订单总数
     */
    Integer orderCountSum(Map map);

    /**
     * 获取销量Top10
     *
     * @param map
     */
    List<GoodsSalesDTO> top10(Map map);

    /**
     * 查询全部订单
     * @return
     */
    @Select("select  count(number) from sky_take_out.orders")
    Integer countAll();
}
