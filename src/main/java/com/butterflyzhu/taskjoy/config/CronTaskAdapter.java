package com.butterflyzhu.taskjoy.config;

import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.support.CronTrigger;

public class CronTaskAdapter extends CronTask implements TaskInfo {
    private String taskName;
    private TaskStatus taskStatus;

    public CronTaskAdapter(Runnable runnable, String expression, String taskName, TaskStatus taskStatus) {
        super(runnable, expression);
        this.taskName = taskName;
        this.taskStatus = taskStatus;
    }

    public CronTaskAdapter(Runnable runnable, CronTrigger cronTrigger, String taskName, TaskStatus taskStatus) {
        super(runnable, cronTrigger);
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

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }
}
