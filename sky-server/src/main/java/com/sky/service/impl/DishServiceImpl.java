package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.BaseException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    public Integer count(Long id) {
        return dishMapper.count(id);
    }

    /**
     * 保存菜品信息
     *
     * @param dishDTO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(DishDTO dishDTO) {
        Dish dish = Dish.builder().build();
        BeanUtils.copyProperties(dishDTO, dish);
        //保存菜品信息
        dishMapper.sava(dish);
        log.info("dishId{}", dish.getId());
        //苍穹外卖规定如果菜品的口味信息为空就不能添加菜品
        log.info("添加菜品信息{}", dishDTO.getFlavors().size());
        log.info("添加菜品信息{}", dishDTO.getFlavors());
        if (dishDTO.getFlavors() == null || dishDTO.getFlavors().size() == 0) {
            throw new BaseException("口味信息为空不能保存!");
        }
        //保存菜品口味信息
        List<DishFlavor> flavors = dishDTO.getFlavors().stream().map(
                dishFlavor -> {
                    dishFlavor.setDishId(dish.getId());
                    return dishFlavor;
                }
        ).collect(Collectors.toList());
        log.info("flavors{}", flavors);
        dishFlavorMapper.batchSave(flavors);
    }

    /**
     * 分页查询菜品信息一条sql进行多表查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageMany(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> conditionByCategory = dishMapper.findConditionByCategory(dishPageQueryDTO);
        return PageResult.builder()
                .total(conditionByCategory.getTotal())
                .records(conditionByCategory.getResult())
                .build();
    }


    /**
     * 分页查询菜品信息 对表进行拆分 两条sql进行查询
     *
     * @param dishPageQueryDTO
     */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        //设置分页条件
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        //条件查询
        Page<Dish> dishPage = dishMapper.findCondition(dishPageQueryDTO);
        log.info("dish{}", dishPage);
        //遍历dishpage通过category_id查询出分类的名字把数据封装到List<DishVO>
        List<DishVO> dishVOS = dishPage.stream().map(
                dish -> {
                    log.info("id{}", dish.getId());
                    DishVO dishVO = new DishVO();
                    BeanUtils.copyProperties(dish, dishVO);
                    //根据category_id查询category
                    Category category = categoryMapper.getById(dish.getCategoryId());
                    log.info("category{}", category);
                    //把category中name赋值给dishVo
                    if (category != null) {
                        dishVO.setCategoryName(category.getName());
                    }
                    return dishVO;
                }
        ).collect(Collectors.toList());
        //放回数据
        return PageResult.builder()
                .total(dishPage.getTotal())
                .records(dishVOS)
                .build();
    }

    /**
     * 批量删除菜品信息
     *
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelete(List<Long> ids) {
        //1.先删除菜品信息
        //1.1.苍穹业务规定 如果菜品存在起售状态 就不能删除
        Integer statusCount = dishMapper.findStatusById(ids);
        if (statusCount > 0) {
            //如果条数大于0的化就抛出异常
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }
        //1.2 如果菜品存在套餐关系也不能删除
        Integer setemalDishCount = setmealDishMapper.findByDishId(ids);
        if (setemalDishCount > 0) {
            //如果条数大于0的化就抛出异常
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //1.3删除菜品信息
        dishMapper.BatchDelete(ids);
        //3.在删除口味信息
        dishFlavorMapper.batchDelete(ids);
    }


    /**
     * 根据ID查询菜品信息
     *
     * @param id
     * @return
     */
    @Override
    public DishVO getById(Long id) {
        //方式一 两条sql语句
        ////获取菜品信息
        //Dish dishId = Dish.builder().id(id).build();
        //Dish dish = dishMapper.findByIdOrCategoryId(dishId).get(0);
        ////根据菜品ID获取口味信息
        //List<DishFlavor> dishFlavors =dishFlavorMapper.finByDishId(id);
        //DishVO dishVO = new DishVO();
        //BeanUtils.copyProperties(dish,dishVO);
        //dishVO.setFlavors(dishFlavors);
        //方式二 一条sql语句 多表查询
        DishVO dishVO = dishMapper.findDishesWithFlavors(id);
        return dishVO;
    }


    /**
     * 菜品修改
     *
     * @param dishDTO
     */
    @Override
    public void update(DishDTO dishDTO) {
        //1.修改菜品信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
        //2.修改口味信息
        //2.1.先删除菜品对应的口味信息在保存
        //2.2判断口味信息是否为空就不保存口味信息
        if(dishDTO.getFlavors() == null || dishDTO.getFlavors().size() ==0 ){
            return;
        }
        List<Long> ids = new ArrayList<>();
        ids.add(dishDTO.getId());
        dishFlavorMapper.batchDelete(ids);
        //2.3因为前端新增的口味信息没有绑定菜品信息所以需要设置菜品id
        List<DishFlavor> flavors = dishDTO.getFlavors().stream().map(
                dishFlavor -> {
                    dishFlavor.setDishId(dishDTO.getId());
                    return dishFlavor;
                }
        ).collect(Collectors.toList());
        //2.4保存口味信息
        dishFlavorMapper.batchSave(flavors);
    }


    /**
     * 通过分类ID获取对应所有的菜品
     * @param dish
     * @return
     */
    @Override
    public List<DishVO> findCategoryId(Dish dish) {
        List<DishVO> dishList = dishMapper.findByIdOrCategoryId(dish);
        return dishList;
    }
}
