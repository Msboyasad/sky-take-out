package com.sky.controller.admin;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "套餐相关接口")
@RestController
@RequestMapping("/admin/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐接口
     *
     * @param setmealDTO
     * @return
     */
    @ApiOperation("新增套餐接口")
    @PostMapping
    @CacheEvict(cacheNames = "setmealCache", key = "#setmealDTO.getCategoryId()")
    public Result add(@RequestBody SetmealDTO setmealDTO) {
        setmealService.add(setmealDTO);
        return Result.success();
    }

    /**
     * 分页套餐查询接口
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @ApiOperation("分页套餐查询接口")
    @GetMapping("/page")
    public Result page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("setmealPageQueryDTO{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 修改套餐信息接口
     *
     * @param setmealDTO
     * @return
     */
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    @ApiOperation("修改套餐信息接口")
    @PutMapping
    public Result update(@RequestBody SetmealDTO setmealDTO) {

        setmealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * 根据ID获取套餐信息
     *
     * @param id
     * @return
     */
    @ApiOperation("根据ID获取套餐信息接口")
    @GetMapping("/{id}")
    public Result updateById(@PathVariable Long id) {
        SetmealVO setmealVO = setmealService.findSetmealWithCategory(id);
        return Result.success(setmealVO);
    }


    /**
     * 修改套餐信息状态
     *
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("修改套餐信息状态")
    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result updateStatus(@PathVariable Integer status, Long id) {
        SetmealDTO setmealDTO = new SetmealDTO();
        setmealDTO.setStatus(status);
        setmealDTO.setId(id);
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * 删除套餐信息接口
     *
     * @param ids
     * @return
     */
    @ApiOperation("删除套餐信息接口")
    @DeleteMapping
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result delete(@RequestParam List<Long> ids) {
        setmealService.batchDelete(ids);
        return Result.success();
    }

}
