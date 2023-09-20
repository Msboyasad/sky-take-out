package com.sky.task;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class SpingTaskDemo {

   // @Scheduled(cron = "0/5 * * * * ? ")//每5秒执行一次的cron表达式为:0/5 * * * * ?
    public void test() {
        log.info("执行任务调度{}", LocalDateTime.now());
    }
}
