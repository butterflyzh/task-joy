package com.butterflyzhu.taskjoy.config;

import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.Task;

import java.util.concurrent.ScheduledFuture;

public final class ScheduledTask2 {
    private final Task task;
    volatile ScheduledFuture<?> future;

    public ScheduledTask2(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }

    public ScheduledFuture<?> getScheduledFuture() {
        return this.future;
    }
}
