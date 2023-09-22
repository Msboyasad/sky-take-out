package com.sky.service;

import com.sky.dto.GoodsSalesDTO;
import com.sky.vo.*;

import java.time.LocalDate;

public interface ReportService {


    /**
     *营业额统计
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end);


    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    UserReportVO userStatistics(LocalDate begin, LocalDate end);


    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO ordersStatistics(LocalDate begin, LocalDate end);


    /**
     * 商品Top10
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO commodityTop10(LocalDate begin, LocalDate end);


    /**
     * 导出Excel报表
     */
    void export();


    /**
     * 导出Excel报表方式2
     */
    void export2();
}
