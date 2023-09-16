package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.IdDTO;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class AddressBookServiceImpl implements AddressBookService {


    @Resource
    private AddressBookMapper addressBookServiceMapper;

    /**
     * 添加收货地址
     *
     * @param addressBook
     */
    @Override
    public void add(AddressBook addressBook) {
        //首先判断用户有没有收获地址，没有默认为默认地址
        Long userId = BaseContext.get();
        Integer count = addressBookServiceMapper.findUserId(userId);
        addressBook.setIsDefault(0);
        addressBook.setUserId(userId);
        if (count == 0) {
            log.info("该用户第一次添加地址");
            addressBook.setIsDefault(1);
        }
        addressBookServiceMapper.add(addressBook);
    }


    /**
     * 查询当前登录用户的所有地址信息
     *
     * @return
     */
    @Override
    public List<AddressBook> list() {
        return addressBookServiceMapper.findAll(BaseContext.get());
    }


    /**
     * 查询默认地址
     *
     * @return
     */
    @Override
    public AddressBook defaultAddress() {
        return addressBookServiceMapper.findDefault(BaseContext.get());
    }

    /**
     * 根据id修改地址
     *
     * @param addressBook
     */
    @Override
    public void update(AddressBook addressBook) {
        addressBookServiceMapper.update(addressBook);
    }


    /**
     * 根据id查询地址
     *
     * @param id
     * @return
     */
    @Override
    public AddressBook findById(Long id) {
        return addressBookServiceMapper.findById(id);
    }

    /**
     * 根据id删除地址
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        addressBookServiceMapper.delete(id);
    }


    /**
     * 设置默认地址
     *
     * @param id
     */
    @Override
    public void updateDefault(IdDTO id) {
        //先将所有地址设为0
        AddressBook addressBook = AddressBook.builder()
                .isDefault(0)
                .userId(BaseContext.get())
                .build();
        addressBookServiceMapper.update(addressBook);
        //讲当前地址设置为1
        addressBook.setIsDefault(1);
        addressBook.setId(id.getId());
        addressBookServiceMapper.update(addressBook);
    }
}
