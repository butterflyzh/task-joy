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

import java.util.Set;
import java.util.concurrent.ScheduledFuture;

@Component
public class MyTask {

    @Autowired
    private ScheduledAnnotationBeanPostProcessor2 scheduledAnnotationBeanPostProcessor;

    @Scheduled2(value = "task1", cron = "*/5 * * * * ?")
    public void task() {
        System.out.println("执行了");
    }

    @Scheduled2(value = "task2", cron = "*/10 * * * * ?")
    public void task2() {
        System.out.println("执行了2");
        Set<ScheduledTask2> tasks = scheduledAnnotationBeanPostProcessor.getScheduledTasks();
        System.out.println(tasks.size());
        for (ScheduledTask2 scheduledTask2 : tasks) {
            if (scheduledTask2.getTask() instanceof CronTaskAdapter && scheduledTask2.getScheduledFuture() != null) {
                CronTaskAdapter cronTaskAdapter = (CronTaskAdapter) scheduledTask2.getTask();
                System.out.println("任务: " + cronTaskAdapter.getTaskName());
                ScheduledFuture<?> future = scheduledTask2.getScheduledFuture();
                if (future.isCancelled()) {
                    System.out.println("取消");
                }
                if (future.isDone()) {
                    System.out.println("完成");
                }
                System.out.println(future.isCancelled() + " " + future.isDone());
            }
        }
    }
}
