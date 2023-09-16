package com.sky.service.impl;

import com.alibaba.fastjson.serializer.BeanContext;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {


    @Resource
    private ShoppingCartMapper shoppingCartMapper;

    @Resource
    private DishMapper dishMapper;

    @Resource
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车信息
     */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //补全信息
        ShoppingCart shoppingCart = new ShoppingCart();
        Long userId = BaseContext.get();
        shoppingCart.setCreateTime(LocalDateTime.now());
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(userId);
        //判断数据中是否存在购物车信息 存在就数量+1
        ShoppingCart dbShoppingCart = shoppingCartMapper.find(shoppingCart);
        if (dbShoppingCart != null) {
            //数据中已有数据就数量+1
            dbShoppingCart.setNumber(dbShoppingCart.getNumber() + 1);
            //修改数据
            shoppingCartMapper.update(dbShoppingCart);
            return;
        }
        shoppingCart.setNumber(1);
        //没有就添加购物车信息
        // 判断是添加菜品还是套餐
        if (shoppingCart.getDishId() != null) {
            //补充 name image 数量
            //根据菜品ID查询菜品信息
            Dish dish = dishMapper.findById(shoppingCart.getDishId());
            shoppingCart.setName(dish.getName());
            shoppingCart.setImage(dish.getImage());
            shoppingCart.setAmount(dish.getPrice());
        } else {
            //添加套餐信息
            SetmealVO setmeal = setmealMapper.findSetmealWithCategory(shoppingCart.getSetmealId());
            shoppingCart.setName(setmeal.getName());
            shoppingCart.setImage(setmeal.getImage());
            shoppingCart.setAmount(setmeal.getPrice());

        }
        //添加购物车信息
        shoppingCartMapper.add(shoppingCart);
    }

    /**
     * 获取用户中购物车信息
     *
     * @return
     */
    @Override
    public List<ShoppingCart> list() {
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.findByUserIdAll(BaseContext.get());
        return shoppingCartList;
    }


    /**
     * 清空用户购物车
     */
    @Override
    public void clean() {
        shoppingCartMapper.clean(BaseContext.get());
    }


    /**
     * 删除购物车中的一个商品
     *
     * @param shoppingCartDTO
     */
    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        //判断商品信息是否为1
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.get());
        shoppingCart = shoppingCartMapper.findSub(shoppingCart);
        //只有一个数量的话就直接删除购物车
        if(shoppingCart.getNumber()==1){
            shoppingCartMapper.remove(shoppingCart.getId());
            return;
        }
        //否则就数量-1
        shoppingCart.setNumber(shoppingCart.getNumber()-1);
        shoppingCartMapper.update(shoppingCart);
    }
}
