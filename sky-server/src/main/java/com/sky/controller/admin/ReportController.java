package com.sky.controller.admin;


import com.alibaba.excel.EasyExcel;
import com.sky.dto.GoodsSalesDTO;
import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;

@RestController
@RequestMapping("/admin/report")
@Slf4j
@Api(tags = "数据报表相关接口")
public class ReportController {

    @Resource
    private ReportService reportService;


    /**
     * 营业额统计接口
     *
     * @param begin
     * @param end
     * @return
     */
    @ApiOperation("营业额统计接口")
    @GetMapping("/turnoverStatistics")
    public Result turnoverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                     @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        log.info("营业额统计{},{}", begin, end);
        TurnoverReportVO turnoverReportVO = reportService.turnoverStatistics(begin, end);
        return Result.success(turnoverReportVO);
    }


    /**
     * 用户统计接口
     *
     * @param begin
     * @param end
     * @return
     */
    @ApiOperation("用户统计接口")
    @GetMapping("/userStatistics")
    public Result userStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                 @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("用户统计接口{}，{}", begin, end);
        UserReportVO userReportVO = reportService.userStatistics(begin, end);
        return Result.success(userReportVO);
    }


    /**
     * 订单统计接口
     *
     * @param begin
     * @param end
     * @return
     */
    @ApiOperation("订单统计接口")
    @GetMapping("/ordersStatistics")
    public Result ordersStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                   @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("订单统计接口{}，{}", begin, end);
        OrderReportVO orderReportVO = reportService.ordersStatistics(begin, end);
        return Result.success(orderReportVO);
    }


    /**
     * 商品Top10接口
     *
     * @param begin
     * @param end
     * @return
     */
    @ApiOperation("商品Top10接口")
    @GetMapping("/top10")
    public Result Top10(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("商品Top10");
        SalesTop10ReportVO top10 = reportService.commodityTop10(begin, end);
        return Result.success(top10);
    }

    /**
     * 导出Excel报表接口
     *
     * @return
     */
    @ApiOperation("导出Excel报表接口")
    @GetMapping("/export")
    public String export() {
        //reportService.export();//poi 方式 如果数据量过多会导致内存溢出
        reportService.export2();//EasyExcel 方式 可以进行分页查询
        return "OK";
    }
}
