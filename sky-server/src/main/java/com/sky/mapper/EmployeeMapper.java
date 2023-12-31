package com.sky.mapper;

import com.sky.annotation.Autofill;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 添加员工信息
     * @param employee
     */
    @Autofill(OperationType.INSERT)
    void add(Employee employee);

    /**
     * 根据员工姓名查询信息
     * @param name
     * @return
     */
    List<Employee> getByName(String name);

    /**
     * 修改员工信息
     * @param employee
     */
    @Autofill(OperationType.UPDATE)
    void update(Employee employee);


    @Select("select * from employee where id = #{id}")
    Employee getById(Integer id);


    /**
     * 查找密码是否正确
     * @param passwordEditDTO
     */
    @Select("select*  from sky_take_out.employee where id = #{empId} and password =#{oldPassword}")
    Employee password(PasswordEditDTO passwordEditDTO);

    /**
     * 修改密码
     * @param passwordEditDTO
     */
    @Update("update employee set password = #{newPassword} where id = #{empId} ")
    void upadetPasword(PasswordEditDTO passwordEditDTO);
}
