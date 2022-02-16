package com.butterflyzhu.taskjoy.task;

import com.butterflyzhu.taskjoy.annotation.Scheduled2;
import com.butterflyzhu.taskjoy.annotation.ScheduledAnnotationBeanPostProcessor2;
import com.butterflyzhu.taskjoy.config.CronTaskAdapter;
import com.butterflyzhu.taskjoy.config.ScheduledTask2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class MyTask {

    @Scheduled2(value = "task1", cron = "*/5 * * * * ?")
    public void task() {
        System.out.println("task1 cron " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ": 执行了");
    }

    @Scheduled2(value = "task2", fixedDelay = 3, initialDelay = 2, timeUnit = TimeUnit.SECONDS)
    public void tas2() {
        System.out.println("task2 fixedDelay " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ": 执行了");
    }

    @Scheduled2(value = "task3", fixedRate = 3, initialDelay = 1, timeUnit = TimeUnit.SECONDS)
    public void tas3() {
        System.out.println("task3 fixedRate " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ": 执行了");
    }
}
