package com.butterflyzhu.taskjoy.concurrent;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SimpleTaskScheduler implements TaskScheduler {

    private ScheduledExecutorService scheduledExecutor;

    public SimpleTaskScheduler(ScheduledExecutorService scheduledExecutor) {
        this.scheduledExecutor = scheduledExecutor;
    }

    public void setScheduledExecutor(ScheduledExecutorService scheduledExecutor) {
        this.scheduledExecutor = scheduledExecutor;
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable task, Trigger trigger) {
        return new ReschedulingRunnable2(task, trigger, getClock(), scheduledExecutor).scheduled();
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable task, Date startTime) {
        long initialDelay = startTime.getTime() - this.getClock().millis();
        return this.scheduledExecutor.schedule(task, initialDelay, TimeUnit.MILLISECONDS);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
        long initialDelay = startTime.getTime() - this.getClock().millis();

        return this.scheduledExecutor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
        return this.scheduledExecutor.scheduleAtFixedRate(task, 0L, period, TimeUnit.MILLISECONDS);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Date startTime, long delay) {
        long initialDelay = startTime.getTime() - this.getClock().millis();

        return this.scheduledExecutor.scheduleWithFixedDelay(task, initialDelay, delay, TimeUnit.MILLISECONDS);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long delay) {
        return this.scheduledExecutor.scheduleWithFixedDelay(task, 0L, delay, TimeUnit.MILLISECONDS);
    }
}
