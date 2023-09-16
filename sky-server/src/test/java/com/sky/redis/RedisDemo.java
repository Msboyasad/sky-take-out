package com.sky.redis;


import com.sky.result.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

@SpringBootTest
public class RedisDemo {


    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    /**
     * 操作字符串类型
     */
    public void test(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("name","张三");
        valueOperations.set("age",20);
        String name = (String) valueOperations.get("name");
        Integer age = (Integer) valueOperations.get("age");
        System.out.println(name + " \t" + age);
        HashMap<Object, Object> map = new HashMap<>();
    }

    /**
     * 操作哈希类型
     */
    @Test
    public  void test2(){
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put("100","nanme","tom");
        hashOperations.put("100","age",20);
        System.out.println(hashOperations.get("100", "nanme"));
        System.out.println(hashOperations.get("100", "age"));
        System.out.println(hashOperations.keys("100"));
        System.out.println(hashOperations.values("100"));
        hashOperations.delete("100","age");
    }


    /**
     * list操作类型
     */
    @Test
    public void   demo3(){
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.leftPush("list","a");
        listOperations.leftPushAll("list","b","c","d","e");
        System.out.println(listOperations.range("list",0,-1));
        listOperations.leftPop("list");
        System.out.println(listOperations.size("list"));
    }


    /**
     * 操作set类型
     */
    @Test
    public void test4(){
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.add("set1",1,2,3,4,5,6);
        setOperations.add("set2",1,2,3,7,8,9);
        System.out.println(setOperations.members("set1"));
        System.out.println(setOperations.size("set2"));
        System.out.println(setOperations.intersect("set1","set2"));
        System.out.println(setOperations.union("set1","set2"));
        setOperations.remove("set1","set2");
    }


    /**
     * zset 有序类型
     */
    @Test
    public void test5(){
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add("1","a",10);
        zSetOperations.add("1","b",11);
        zSetOperations.add("1","c",12);
        zSetOperations.add("1","d",13);
        System.out.println(zSetOperations.range("1",0,-1));
        zSetOperations.incrementScore("1","c",100);
        zSetOperations.remove("1","a","b");
        System.out.println(zSetOperations.range("1",0,-1));
    }

    /**
     * 通用操作命令
     */
    @Test
    public void test6(){
        Set keys = redisTemplate.keys("*");
        System.out.println(keys);//获取所有的键
        System.out.println(redisTemplate.hasKey("name"));//判断key是否存在
        for (Object key : keys) {
            System.out.println(redisTemplate.type(key).name());//获取key的数据类型
        }
        redisTemplate.delete("1");//删除
    }
}
