package com.sky.service.impl;

import com.sky.entity.DishStatus;
import com.sky.entity.Orders;
import com.sky.entity.SetmealStatus;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author XiaLiu
 * @Date 2023-09-20 20:28
 */

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private SetmealMapper setmealMapper;
    @Resource
    private DishMapper dishMapper;


    /**
     * 查询套餐总览
     *
     * @return
     */
    @Override
    public SetmealOverViewVO overviewSetmeals() {
        //已停售套餐数量 discontinued
        Integer discontinued = 0;
        //已起售数量 sold
        Integer sold = 0;
        List<SetmealStatus> setmealStatusList = setmealMapper.countStatus();
        for (SetmealStatus setmealStatus : setmealStatusList) {
            if (setmealStatus.getStatus() == 0) {
                discontinued = setmealStatus.getNumber();
            } else {
                sold = setmealStatus.getNumber();
            }
        }
        return SetmealOverViewVO
                .builder().discontinued(discontinued)
                .sold(sold)
                .build();
    }

    /**
     * 查询今日运营数据
     *
     * @return
     */
    @Override
    public BusinessDataVO businessData() {
        //获取今日的日期
        LocalDate now = LocalDate.now();
        LocalDateTime startTime = LocalDateTime.of(now, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(now, LocalTime.MAX);
        //获取今日新增用户数
        Map map = new HashMap();
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        Integer newUsers = userMapper.countSum(map);
        //获取今日所有订单
        Integer toDayOrders = orderMapper.orderCountSum(map);
        //获取今日有效订单数
        map.put("status", Orders.COMPLETED);
        Integer validOrderCount = orderMapper.orderCountSum(map);
        //计算订单完成率 今日有效订单数/当日所有订单
        Double orderCompletionRate = (validOrderCount * 1.0) / toDayOrders;
        //今日营业额
        Double turnover = orderMapper.countSum(map);
        //计算平均客单价 订单总金额/成交订单总人数
        Double unitPrice = (turnover * 1.0) / validOrderCount;
        return BusinessDataVO.builder()
                .newUsers(newUsers)
                .orderCompletionRate(orderCompletionRate)
                .turnover(turnover)
                .unitPrice(unitPrice)
                .validOrderCount(validOrderCount)
                .build();
    }


    /**
     * 查询菜品总览
     *
     * @return
     */
    @Override
    public DishOverViewVO overviewDishes() {
        List<DishStatus> dishStatuses = dishMapper.countStatus();
        Integer discontinued = 0;
        Integer sold = 0;
        for (DishStatus dishStatus : dishStatuses) {
            if (dishStatus.getStatus() == 0) {
                discontinued = dishStatus.getNumber();
            } else {
                sold = dishStatus.getNumber();
            }
        }
        return DishOverViewVO.builder()
                .discontinued(discontinued)
                .sold(sold)
                .build();
    }


    /**
     * 查询订单管理数据
     *
     * @return
     */
    @Override
    public OrderOverViewVO overviewOrders() {
        //全部订单 allOrders
        Integer allOrders = orderMapper.countAll();
        //已取消数量  cancelledOrders
        Integer cancelledOrders = orderMapper.count(Orders.CANCELLED);

        //已完成数量 completedOrders
        Integer completedOrders = orderMapper.count(Orders.COMPLETED);

        //带派送数量 deliveredOrders
        Integer deliveredOrders = orderMapper.count(Orders.CONFIRMED);

        //待接单数量  waitingOrders
        Integer waitingOrders = orderMapper.count(Orders.TO_BE_CONFIRMED);
        return OrderOverViewVO.builder()
                .allOrders(allOrders)
                .cancelledOrders(cancelledOrders)
                .completedOrders(completedOrders)
                .deliveredOrders(deliveredOrders)
                .waitingOrders(waitingOrders)
                .build();
    }
}
