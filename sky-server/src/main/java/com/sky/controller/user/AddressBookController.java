package com.sky.controller.user;


import com.sky.dto.IdDTO;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "收获地址相关接口")
@RestController
@RequestMapping("/user/addressBook")
@Slf4j
public class AddressBookController {


    @Resource
    private AddressBookService addressBookService;


    /**
     * 添加收货地址接口
     *
     * @param addressBook
     * @return
     */
    @ApiOperation("添加收货地址接口")
    @PostMapping
    public Result addressBook(@RequestBody AddressBook addressBook) {

        addressBookService.add(addressBook);
        return Result.success();

    }


    /**
     * 获取当前用户所有地址
     *
     * @return
     */
    @ApiOperation("获取当前用户所有地址接口")
    @GetMapping("/list")
    public Result list() {
        List<AddressBook> addressBookList = addressBookService.list();
        return Result.success(addressBookList);

    }


    @ApiOperation("获取当前用户默认地址接口")
    @GetMapping("/default")
    public Result defaultAddress() {
        AddressBook addressBook = addressBookService.defaultAddress();
        return Result.success(addressBook);
    }

    @ApiOperation("根据id修改地址接口")
    @PutMapping
    public Result update(@RequestBody AddressBook addressBook) {
        addressBookService.update(addressBook);
        return Result.success();
    }


    @ApiOperation("根据id查询地址接口")
    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.findById(id);
        return Result.success(addressBook);
    }

    @ApiOperation("根据id删除地址接口")
    @DeleteMapping
    public Result delete(Long id) {
        addressBookService.delete(id);
        return Result.success();
    }

    @ApiOperation("设置默认地址接口")
    @PutMapping("/default")
    public Result updateDefault(@RequestBody IdDTO id) {
        addressBookService.updateDefault(id);
        return Result.success();
    }

}
