package com.sky;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import com.sky.utils.DemoDataListener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Description TODO
 * @Author XiaLiu
 * @Date 2023-09-21 23:22
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ExcelDemo {

    @Autowired
    private CategoryService categoryService;

    /**
     * 一次性全部读取
     */
    @Test
    public void test1() {
        String path = ExcelDemo.class.getClassLoader().getResource("excel/category_template.xlsx").getPath();
        String fileName = "C:/Users/24670/Desktop/a.xlsx";

        EasyExcel.write(fileName).withTemplate(path).sheet().doFill(categoryService.list());
    }

    /**
     * 分页读取
     */

    @Test
    public void test2() {
        String path = ExcelDemo.class.getClassLoader().getResource("excel/category_template.xlsx").getPath();
        String fileName = "C:/Users/24670/Desktop/d.xlsx";

        //分页写入
        try (ExcelWriter excelWriter = EasyExcel.write(fileName).build();
        ) {
            for (int i = 1; i < 10; i++) {
                CategoryPageQueryDTO categoryPageQueryDTO = new CategoryPageQueryDTO();
                categoryPageQueryDTO.setPage(i);
                categoryPageQueryDTO.setPageSize(2);
                PageResult page = categoryService.page(categoryPageQueryDTO);
                WriteSheet writeSheet = EasyExcel.writerSheet().build();
                List records = page.getRecords();
                excelWriter.write(records, writeSheet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 读取excel数据
     */
    @Test
    public void test3(){
        String path = ExcelDemo.class.getClassLoader().getResource("excel/categoryList.xlsx").getPath();
        EasyExcel.read(path,Category.class,new DemoDataListener(categoryService)).sheet().doRead();
    }
}


