package com.sky.mapper;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 分页查询分类管理
     * @return
     */
    List<Category> page(Category category);

    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);

    void update(Category category);

    void add(Category category);
}
