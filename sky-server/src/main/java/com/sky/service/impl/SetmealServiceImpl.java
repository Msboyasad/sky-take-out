package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 统计套餐数量
     *
     * @param id
     * @return
     */
    @Override
    public Integer count(Long id) {
        return setmealMapper.count(id);
    }

    /**
     * 添加套餐
     *
     * @param setmealDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SetmealDTO setmealDTO) {
        //先添加套餐信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.save(setmeal);
        //把菜品信息添加到setmeal_dish中间表
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes().stream().map(
                setmealDish -> {
                    setmealDish.setSetmealId(setmeal.getId());
                    return setmealDish;
                }
        ).collect(Collectors.toList());
        setmealDishMapper.batchSave(setmealDishes);

    }


    /**
     * 分页查询套餐信息
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> setmealVOPage = setmealMapper.findCondition(setmealPageQueryDTO);
        return PageResult.builder()
                .total(setmealVOPage.getTotal())
                .records(setmealVOPage.getResult())
                .build();
    }


    /**
     * 修改套餐信息
     *
     * @param setmealDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SetmealDTO setmealDTO) {
        //先修改套餐信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);
        if (setmealDTO.getSetmealDishes() == null || setmealDTO.getSetmealDishes().size() == 0) {
            return;
        }
        //在修改菜品信息
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        //先删除
        setmealDishMapper.delete(setmealDTO.getId());
        log.info("setmealDishes{}", setmealDishes);
        setmealDishes = setmealDishes.stream().map(
                setmealDish -> {
                    setmealDish.setSetmealId(setmealDTO.getId());
                    return setmealDish;
                }
        ).collect(Collectors.toList());
        //在保存
        log.info("setmealDishes{}", setmealDishes);
        setmealDishMapper.batchSave(setmealDishes);
    }


    /**
     * 根据ID获取套餐信息
     *
     * @param id
     * @return
     */
    @Override
    public SetmealVO findSetmealWithCategory(Integer id) {
        SetmealVO setmealVO = setmealMapper.findSetmealWithCategory(id);
        return setmealVO;
    }

    /**
     * 删除套餐信息
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        Integer setmealCount = setmealMapper.batchCount(ids);
        if (setmealCount > 0) {
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }
        //删除套餐表中数据
        setmealMapper.delete(ids);
        //删除套餐关系表
        setmealDishMapper.batchDelete(ids);

    }
}
