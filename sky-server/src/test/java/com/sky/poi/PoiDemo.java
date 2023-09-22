package com.sky.poi;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @Description TODO
 * @Author XiaLiu
 * @Date 2023-09-20 21:50
 */
public class PoiDemo {

    /**
     * Excel写出
     */

    @Test
    public void test1() throws Exception {
        //创建 XSSFWorkbook对象
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建sheet对象
        XSSFSheet sheet = workbook.createSheet("itcast");
        //创建行
        XSSFRow row0 = sheet.createRow(0);
        XSSFRow row1 = sheet.createRow(1);
        XSSFRow row2 = sheet.createRow(2);
        XSSFRow row3 = sheet.createRow(3);
        //创建列 写入数据
        row0.createCell(1).setCellValue("姓名");
        row0.createCell(2).setCellValue("爱好");

        row1.createCell(1).setCellValue("张三");
        row1.createCell(2).setCellValue("篮球");

        row2.createCell(1).setCellValue("李四");
        row2.createCell(2).setCellValue("足球");

        row3.createCell(1).setCellValue("王五");
        row3.createCell(2).setCellValue("羽毛球");
        //创建输出流
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("C:\\Users\\24670\\Desktop\\itcast.xlsx"));
        //写出
        workbook.write(out);
        //关流
        out.close();
        workbook.close();
    }

    /**
     * 读取Excel文件
     */
    @Test
    public void test2() throws Exception {
        //读取文件
        BufferedInputStream bus = new BufferedInputStream(new FileInputStream("C:\\Users\\24670\\Desktop\\itcast.xlsx"));
        //创建XSSFWorkbook
        XSSFWorkbook workbook = new XSSFWorkbook(bus);
        //选择工作表
        XSSFSheet sheet = workbook.getSheetAt(0);
        //读取最后一行行号
        int lastRowNum = sheet.getLastRowNum();
        //遍历
        for (int i = 0; i < lastRowNum +1; i++) {
            //获取行
            XSSFRow row = sheet.getRow(i);
            //获取数据
            String name = row.getCell(1).getStringCellValue();
            String hobby = row.getCell(2).getStringCellValue();
            System.out.println(name + ":" + hobby);
        }
        workbook.close();
        bus.close();
    }
}
