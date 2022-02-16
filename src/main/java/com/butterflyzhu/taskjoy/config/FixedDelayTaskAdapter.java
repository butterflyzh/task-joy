package com.butterflyzhu.taskjoy.config;

import org.springframework.scheduling.config.FixedDelayTask;

public class FixedDelayTaskAdapter extends FixedDelayTask implements TaskInfo {
    private String taskName;
    private TaskStatus taskStatus;

    public FixedDelayTaskAdapter(Runnable runnable, long interval, long initialDelay) {
        super(runnable, interval, initialDelay);
    }

    public FixedDelayTaskAdapter(Runnable runnable, long interval, long initialDelay, String taskName, TaskStatus taskStatus) {
        super(runnable, interval, initialDelay);
        this.taskName = taskName;
        this.taskStatus = taskStatus;
    }

    @Override
    public String getTaskName() {
        return this.taskName;
    }

    @Override
    public TaskStatus getTaskStatus() {
        return this.taskStatus;
    }
}
