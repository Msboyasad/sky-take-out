package com.sky.aspect;


import com.sky.annotation.Autofill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import com.sky.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 切面类进行全局公共字段填充
 */
@Component
@Aspect
@Slf4j
public class AutoFillAspect {

    @Before("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.Autofill)")
    public void before(JoinPoint jt) throws Exception {


        log.info("全  局公共字段填充");
        //1.获取原有方法上自定义注解
        //aop获取原有方法签名对象
        MethodSignature signature = (MethodSignature) jt.getSignature();
        //根据原有发放获取自定义注解对象
        Autofill annotation = signature.getMethod().getAnnotation(Autofill.class);
        //2.获取原有签名对象上的自定义注解以及它的值
        OperationType operationType = annotation.value();
        //3.获取加了公共字段注解下方法的参数
        Object[] args = jt.getArgs();
        //4.判断对象是否为空
        if (args == null || args.length == 0) {
            log.error("公共填充字段使用错误，无效使用");
            return;
        }
        //5.准备公共字段填充的数据、获取数组的第一个对象
        Object obj = args[0];
        if(obj instanceof List) {
            List objList = (List) obj;
            objList.forEach(o -> {
                try {
                    add(obj,operationType);
                } catch (Exception e) {
                    throw new BaseException("公共字段填充失败！");
                }
            });
        }
        else {
            add(obj,operationType);
        }


    }
    public  void add(Object obj,OperationType operationType) throws Exception{
        Long userId = BaseContext.get();
        LocalDateTime now = LocalDateTime.now();
        //6.通过反射获取对象所对应的方法
        //如果是添加方法就对创建时间、创建人、修改时间、修改人进行赋值 setUpdateTime
        //7.如果是修改方法就对修改人、修改时间进行赋值
        Method updateTime = obj.getClass().getMethod("setUpdateTime", LocalDateTime.class);
        Method updateUser = obj.getClass().getMethod("setUpdateUser", Long.class);
        updateTime.invoke(obj, now);
        updateUser.invoke(obj, userId);
        if (operationType == OperationType.INSERT) {
            Method createTime = obj.getClass().getMethod("setCreateTime", LocalDateTime.class);
            Method createUser = obj.getClass().getMethod("setCreateUser", Long.class);
            //给方法进行赋值
            createTime.invoke(obj, now);
            createUser.invoke(obj, userId);
        }
    }
}
