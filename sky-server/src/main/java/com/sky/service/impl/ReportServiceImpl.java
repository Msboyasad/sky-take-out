package com.sky.service.impl;


import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.exception.BaseException;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.*;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private UserMapper userMapper;


    /**
     * 营业额统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {

        //1.首先计算出日期
        List<LocalDate> dateTimeList = getLocalDates(begin, end);
        //2.计算每日的营业额
        //2.1遍历日期集合获取每日营业额 状态必需是已完成 COMPLETED = 5
        Map<String, Object> map = new HashMap<>();
        List<Double> turnoverList = dateTimeList.stream().map(
                dateTime -> {
                    //起始时间
                    LocalDateTime startTime = LocalDateTime.of(dateTime, LocalTime.MIN);
                    //结束时间
                    LocalDateTime endTime = LocalDateTime.of(dateTime, LocalTime.MAX);
                    //使用map装载数据

                    //方式1
                    //map.put("startTime", startTime);
                    //map.put("endTime", endTime);
                    //map.put("status", Orders.COMPLETED);
                    //Double amountSum = orderMapper.countSum(map);
                    //方式2
                    map.put("startTime", dateTime);
                    map.put("status", Orders.COMPLETED);
                    Double amountSum = orderMapper.countSum(map);
                    //如果当日没有数据就给0
                    if (ObjectUtils.isEmpty(amountSum)) {
                        amountSum = 0.D;
                    }
                    return amountSum;
                }
        ).collect(Collectors.toList());
        //把集合转换成字符串
        String dateListStr = StringUtils.join(dateTimeList, ",");
        String turnoverListStr = StringUtils.join(turnoverList, ",");
        //返回数据
        return TurnoverReportVO.builder()
                .dateList(dateListStr)
                .turnoverList(turnoverListStr)
                .build();
    }


    /**
     * 订单统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {

        //计算日期
        List<LocalDate> localDateList = getLocalDates(begin, end);
        //计算订单总数
        Map map = new HashMap();
        Integer totalOrderCount = 0; //订单总数
        Integer validOrderCount = 0;//有效订单数
        List<Integer> orderCountList = new ArrayList<>();//订单数列表
        List<Integer> validOrderCountList = new ArrayList<>();//有效订单数列表
        //订单有效数 订单有效数集合
        for (LocalDate localDate : localDateList) {
            //订单有效数
            map.put("startTime", LocalDateTime.of(localDate, LocalTime.MIN));
            map.put("endTime", LocalDateTime.of(localDate, LocalTime.MAX));
            map.put("status", Orders.COMPLETED);
            Integer validOrder = orderMapper.orderCountSum(map);
            validOrderCountList.add(validOrder);
            validOrderCount += validOrder;
            //订单总数
            map.put("status", null);
            Integer totalOrder = orderMapper.orderCountSum(map);
            totalOrderCount += totalOrder;
            orderCountList.add(totalOrder);
        }
        // 有效订单 / 订单总数
        Double orderCompletionRate = 0.0;
        if (totalOrderCount != 0) {
            orderCompletionRate = (validOrderCount * 1.0) / totalOrderCount;
        }
        return OrderReportVO.builder()
                .dateList(StringUtils.join(localDateList, ","))
                .orderCompletionRate(orderCompletionRate)
                .orderCountList(StringUtils.join(orderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .build();
    }

    /**
     * 用户统计
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        //首先计算出日期
        List<LocalDate> dateList = getLocalDates(begin, end);
        Map map = new HashMap();
        //总用户数量
        List<Integer> totalUserList = new ArrayList<>();
        Integer totalCount = 0;
        //计算前一天的日期 因为用户总数量要记录包括前面的所有用户数量
        LocalDateTime baseTime = LocalDateTime.of(dateList.get(0).minusDays(1), LocalTime.MAX);
        map.put("endTime", baseTime);
        totalCount = userMapper.countSum(map); //求出第一个日期之前的用户数量
        //newUserList 计算新增用户数量
        List<Integer> newUserList = new ArrayList<>();
        Integer newCount;
        //遍历日期计算新增用户数
        for (LocalDate localDate : dateList) {
            //计算新增用户数
            map.put("startTime", LocalDateTime.of(localDate, LocalTime.MIN));
            map.put("endTime", LocalDateTime.of(localDate, LocalTime.MAX));
            newCount = userMapper.countSum(map);//当天新增用户数量
            newUserList.add(newCount);
            //总用户数量 等于 当天数量加上前一天数量
            totalCount += newCount;
            totalUserList.add(totalCount);
        }
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }


    /**
     * 获取起始日期到结束日期
     *
     * @param begin
     * @param end
     * @return
     */
    private static List<LocalDate> getLocalDates(LocalDate begin, LocalDate end) {
        List<LocalDate> dateTimeList = new ArrayList<>();
        while (!begin.equals(end.plusDays(1))) {
            dateTimeList.add(begin);
            begin = begin.plusDays(1);
        }
        //1.1健壮性判断
        if (CollectionUtils.isEmpty(dateTimeList)) {
            throw new BaseException("日期格式有误!");
        }
        return dateTimeList;
    }


    /**
     * 商品Top10
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO commodityTop10(LocalDate begin, LocalDate end) {
        Map map = new HashMap();
        LocalDateTime startTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        //设置开始时间和结束时间
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("status", Orders.COMPLETED);
        //获取每天菜品的销售额
        List<GoodsSalesDTO> goodsSalesDTOList = orderMapper.top10(map);
        List<String> nameList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();
        goodsSalesDTOList.forEach(
                salesTop10ReportVO -> {
                    nameList.add(salesTop10ReportVO.getName());
                    numberList.add(salesTop10ReportVO.getNumber());
                }
        );
        //返回数据
        return SalesTop10ReportVO.builder()
                .numberList(StringUtils.join(numberList, ","))
                .nameList(StringUtils.join(nameList, ","))
                .build();
    }
}

