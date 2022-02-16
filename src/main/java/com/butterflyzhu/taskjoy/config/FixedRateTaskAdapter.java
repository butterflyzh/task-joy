package com.butterflyzhu.taskjoy.config;

import org.springframework.scheduling.config.FixedRateTask;

public class FixedRateTaskAdapter extends FixedRateTask implements TaskInfo {
    private String taskName;
    private TaskStatus taskStatus;


    public FixedRateTaskAdapter(Runnable runnable, long interval, long initialDelay, String taskName, TaskStatus taskStatus) {
        super(runnable, interval, initialDelay);
        this.taskName = taskName;
        this.taskStatus = taskStatus;
    }

    @Override
    public String getTaskName() {
        return taskName;
    }

    @Override
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }
}
