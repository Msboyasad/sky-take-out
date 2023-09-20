package com.sky;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;

public class Demo {

    @Test
    public void test(){
        LocalDate begin = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(1);
        System.out.println(begin);
        System.out.println(end);
        while (!begin.equals(end.plusDays(1))) {
            System.out.println("----------------------------");
            begin= begin.plusDays(1);
        }
    }
}
