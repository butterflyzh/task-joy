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

@Component
public class MyTask {

    @Scheduled2(value = "task1", cron = "*/5 * * * * ?")
    public void task() {

        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ": 执行了");
    }
}
