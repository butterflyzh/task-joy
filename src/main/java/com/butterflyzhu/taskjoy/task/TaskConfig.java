package com.butterflyzhu.taskjoy.task;

import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskConfig {

//    @Bean
//    public SchedulingConfigurer schedulingConfigurer() {
//        return taskRegistrar -> {
//            ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("task-schedule-pool-%d").build();
//            ThreadPoolExecutor taskPool = new ScheduledThreadPoolExecutor(5, threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
//            taskPool.setMaximumPoolSize(20);
//            taskPool.setKeepAliveTime(20, TimeUnit.SECONDS);
//            taskRegistrar.setScheduler(taskPool);
//        };
//    }
}
