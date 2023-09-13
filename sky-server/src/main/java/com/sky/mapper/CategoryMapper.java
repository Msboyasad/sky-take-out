package com.sky.mapper;

import com.sky.annotation.Autofill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 分页查询分类管理
     *
     * @return
     */
    List<Category> page(Category category);

    /**
     * 根据id删除分类管理
     *
     * @param id
     */
    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);

    /**
     * 修改分类管理信息
     *
     * @param category
     */
    @Autofill(OperationType.UPDATE)
    void update(Category category);

    /**
     * 添加分类管理信息
     *
     * @param category
     */
    @Autofill(OperationType.INSERT)
    void add(Category category);

    /**
     * 根据iD获取分类信息
     */
    @Select("select * from sky_take_out.category where id = #{id}")
    Category getById(Long id);


    /**
     * 根据类型获取分类信息
     *
     * @param type
     * @return
     */
    @Select("select * from sky_take_out.category where type = #{type}")
    List<Category> listType(Integer type);
}
