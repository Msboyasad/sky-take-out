package com.sky.mapper;


import com.github.pagehelper.Page;
import com.sky.annotation.Autofill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 统计套餐数量
     * @param id
     * @return
     */
    @Select("select count(*) from sky_take_out.setmeal where category_id = #{id}")
    Integer count(Long id);


    /**
     * 添加套餐信息
     * @param setmeal
     */
    @Autofill(OperationType.INSERT)
    void save(Setmeal setmeal);

    Page<SetmealVO> findCondition(SetmealPageQueryDTO setmealPageQueryDTO);


    /**
     * 根据ID查询套餐信息
     * @param id
     * @return
     */

    SetmealVO findSetmealWithCategory(Integer id);


    /**
     * 修改套餐信息
     * @param setmeal
     */
    @Autofill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 统计套餐在售的数量
     * @param ids
     * @return
     */
    Integer batchCount(List<Long> ids);


    /**
     * 删除套餐表中的信息
     * @param ids
     */
    void delete(List<Long> ids);
}
