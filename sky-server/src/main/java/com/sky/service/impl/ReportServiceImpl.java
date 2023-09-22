package com.sky.service.impl;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.sky.constant.MessageConstant;
import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.exception.BaseException;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private WorkspaceService workspaceService;
    @Resource
    private HttpServletResponse response;


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


    /**
     * 导出Excel报表
     */
    @Override
    public void export() {
        //加载文件
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("Template/运营数据报表模板.xlsx");
             XSSFWorkbook workbook = new XSSFWorkbook(in);   //创建XSSFWorkbook对象
             ServletOutputStream out = response.getOutputStream();
        ) {
            //读取sheet
            XSSFSheet sheet = workbook.getSheetAt(0);
            //获取时间
            LocalDate endTime = LocalDate.now().minusDays(1);
            LocalDate startTime = LocalDate.now().minusDays(30);
            //写入时间 第2行第2列
            sheet.getRow(1).getCell(1).setCellValue("时间: " + startTime + " 至 " + endTime);
            //写入概览数据 30天的
            BusinessDataVO businessDataVO = workspaceService.businessData(startTime, endTime);
            //营业额 3行 2列
            sheet.getRow(3).getCell(2).setCellValue(businessDataVO.getTurnover());
            //订单完成率 3 行 4列
            sheet.getRow(3).getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());

            //新增用户数 3行 6列
            sheet.getRow(3).getCell(6).setCellValue(businessDataVO.getNewUsers());

            //有效订单 4行 2列
            sheet.getRow(4).getCell(2).setCellValue(businessDataVO.getValidOrderCount());

            //平均客单价 4行 4列
            sheet.getRow(4).getCell(4).setCellValue(businessDataVO.getUnitPrice());
            //循环30天 写入每一天的数据
            for (int i = 0; i < 30; i++) {
                //获取当天报表数据
                businessDataVO = workspaceService.businessData(startTime, startTime);
                //写入数据
                XSSFRow row = sheet.getRow(7 + i);
                //日期 1
                row.getCell(1).setCellValue(startTime.toString());
                //营业额2
                row.getCell(2).setCellValue(businessDataVO.getTurnover());

                //有效订单 3
                row.getCell(3).setCellValue(businessDataVO.getValidOrderCount());

                //订单完成率 4
                row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());

                //平均客单价 5
                row.getCell(5).setCellValue(businessDataVO.getUnitPrice());

                //新增用户数 6
                row.getCell(6).setCellValue(businessDataVO.getNewUsers());
                //日期加1
                startTime = startTime.plusDays(1);

            }
            //保存数据
            workbook.write(out);

        } catch (IOException e) {
            throw new BaseException("导出Excel报表失败!");
        }

    }


    /**
     * 导出Excel方式2
     */
    @Override
    public void export2() {
        String path = this.getClass().getClassLoader().getResource("excel/data.xlsx").getPath();
        //读取模板文件
        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(path).build()) {
            {
                WriteSheet writeSheet = EasyExcel.writerSheet().build();
                //获取时间
                LocalDate endTime = LocalDate.now().minusDays(1);
                LocalDate startTime = LocalDate.now().minusDays(30);
                //写入时间
                String dateRange = "时间:" + startTime + " 至 " + endTime;
                //写入概览数据 30天的
                BusinessDataVO businessDataVO = workspaceService.businessData(startTime, endTime);
                businessDataVO.setDateRange(dateRange);
                excelWriter.fill(businessDataVO, writeSheet);
                List<BusinessDataVO> businessDataVOList = new ArrayList<>();
                //写入明细数据
                for (int i = 0; i < 30; i++) {
                    //获取当天报表数据
                    businessDataVO = workspaceService.businessData(startTime, startTime);
                    businessDataVO.setDate(startTime.toString());
                    businessDataVOList.add(businessDataVO);
                    //日期加1
                    startTime = startTime.plusDays(1);
                }
                //写入明细数据
                excelWriter.fill(businessDataVOList, writeSheet);
                excelWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(MessageConstant.EXCEL_UPLOAD_FAILED);
        }

    }
}