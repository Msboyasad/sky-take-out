package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

/**
 * @Description TODO
 * @Author XiaLiu
 * @Date 2023-09-20 20:28
 */
public interface WorkspaceService {

    /**
     * 查询今日运营数据
     * @return
     */
    BusinessDataVO businessData();


    /**
     * 查询套餐总览
     * @return
     */
    SetmealOverViewVO overviewSetmeals();


    /**
     * 查询菜品总览
     * @return
     */
    DishOverViewVO overviewDishes();

    /**
     * 查询订单管理数据
     * @return
     */
    OrderOverViewVO overviewOrders();
}
