package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@Api(tags = "员工登陆相关接口")
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 退出
     *
     * @return
     */
    @ApiOperation("员工退出接口")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */

    @PostMapping("/login")
    @ApiOperation(value = "员工登陆接口")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 添加员工信息接口
     *
     * @param employeeDTO
     * @return
     */
    @ApiOperation(value = "添加员工信息接口")
    @PostMapping
    public Result add(@RequestBody EmployeeDTO employeeDTO) {
        boolean flag = employeeService.add(employeeDTO);
        return flag ? Result.success("添加员工信息成功！") : Result.success("添加员工信息失败！");
    }

    /**
     * 员工分页查询接口
     *
     * @param employeePageQueryDTO
     * @return
     */
    @ApiOperation("员工分页查询接口")
    @GetMapping("/page")
    public Result page(EmployeePageQueryDTO employeePageQueryDTO) {
        PageResult page = employeeService.page(employeePageQueryDTO);
        return Result.success(page);
    }

    /**
     * 修改员工状态
     */
    @ApiOperation("修改员工状态接口")
    @PostMapping("/status/{status}")
    public Result status(@PathVariable Integer status, Long id) {
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeService.update(employee);
        return Result.success();
    }

    @ApiOperation("根据Id获取员工信息")
    @GetMapping("{id}")
    public  Result getById(@PathVariable Integer id){
        Employee employee =employeeService.getById(id);
        return Result.success(employee);
    }

    @ApiOperation("修改员工信息")
    @PutMapping
    public Result updateById(@RequestBody  EmployeeDTO employeeDTO){
        Employee employee = Employee.builder().build();
        BeanUtils.copyProperties(employeeDTO,employee);
        log.info("修改员工前端传过来的数据{}",employee);
        log.info("修改员工前端传过来的数据{}",employee);
        employeeService.update(employee);
        return Result.success();
    }


    /**
     *修改密码
     * @param passwordEditDTO
     * @return
     */
    @PutMapping("/editPassword")
    public Result editPassword(@RequestBody PasswordEditDTO passwordEditDTO){
        employeeService.updatePassword(passwordEditDTO);
        return Result.success();
    }

}
