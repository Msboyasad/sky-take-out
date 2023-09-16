package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;


import java.util.List;

public interface ShoppingCartService {
    /**
     * 添加购物车信息
     */
    void add(ShoppingCartDTO shoppingCartDTO);


    /**
     * 获取用户中的购物车信息
     * @return
     */
    List<ShoppingCart> list();


    /**
     * 清空用户购物车
     */
    void clean();


    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO
     */
    void sub(ShoppingCartDTO shoppingCartDTO);
}
