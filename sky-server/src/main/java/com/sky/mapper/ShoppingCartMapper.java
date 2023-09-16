package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {


    /**
     * 根据用户ID和套餐ID或者菜品ID查找数据
     *
     * @param shoppingCart
     * @return
     */
    ShoppingCart find(ShoppingCart shoppingCart);


    /**
     * 修改用户购物信息的数量
     *
     * @param dbShoppingCart
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void update(ShoppingCart dbShoppingCart);


    /**
     * 添加购物车信息
     *
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart values (null,#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{createTime})")
    void add(ShoppingCart shoppingCart);

    /**
     * 根据用户Id查询购物车信息
     *
     * @param userId
     * @return
     */
    @Select("select * from sky_take_out.shopping_cart where user_id =#{userId}")
    List<ShoppingCart> findByUserIdAll(Long userId);


    /**
     * 清空用户购物车
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void clean(Long userId);

    ShoppingCart findSub(ShoppingCart shoppingCart);

    /**
     * 根据I的删除购物车信息
     *
     * @param id
     */
    @Delete("delete from shopping_cart where id =#{id}")
    void remove(Long id);
}
