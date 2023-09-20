package com.sky.task;


import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OrderTask {

    @Resource
    private OrderMapper orderMapper;


    /**
     * 每1分钟定时查询用户未支付的订单
     */
    //@Scheduled(cron = "0 0/1 * * * ?")
    //@Scheduled(cron = "0/1 * * * * ?")
    public void orderTimeoutTimer() {
        log.info("开始检查订单超时.....");
        //下单时间<=当前时间-15就超时 例如 当前时间10：00 -15 = 9：45
        //只要下单时间小于9：45就算超时  例如 ：9：46 就算超时
        //sql语句 select * from orders where status = #{status} and order_time<=#{currentTime}
        LocalDateTime currentTime = LocalDateTime.now().minusMinutes(15);
        List<Orders> ordersList = orderMapper.findByTimeoutTimer(Orders.PENDING_PAYMENT, currentTime);
        //健壮性判断
        if (ordersList == null || ordersList.size() == 0) {
            log.info("当前时间：{}没有超时订单", LocalDateTime.now());
            return;
        }
        //方式1 遍历集合把修改数据
        //方式2 获取集合中的ID 批量修改
        List<Long> ids = ordersList.stream().map(Orders::getId).collect(Collectors.toList());
        orderMapper.updateByTimeoutTimerIds(ids, Orders.CANCELLED, "订单超时,自动取消", LocalDateTime.now());
    }



    /**
     * 每天的凌晨1点查询订单派送中
     */
    //@Scheduled(cron = "0 0 1 * * ?")
    //@Scheduled(cron = "0 0/1 * * * ?")
    public void orderDelivered() {
        log.info("开始检查订单派送中数据.....");
        LocalDateTime currentTime = LocalDateTime.now().minusMinutes(60);
        List<Orders> ordersList = orderMapper.findByDelivered(Orders.DELIVERY_IN_PROGRESS, currentTime);
        //健壮性判断
        if (ordersList == null || ordersList.size() == 0) {
            log.info("当前时间：{}没有派送中数据", LocalDateTime.now());
            return;
        }
        //方式1 遍历集合把修改数据
        //方式2 获取集合中的ID 批量修改
        List<Long> ids = ordersList.stream().map(Orders::getId).collect(Collectors.toList());
        orderMapper.updateByDeliveredIds(ids, Orders.COMPLETED,LocalDateTime.now());
    }
}
