package com.butterflyzhu.taskjoy.concurrent;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.util.Assert;

import java.time.Clock;
import java.util.Date;
import java.util.concurrent.*;

public class ReschedulingRunnable2 implements Runnable, ScheduledFuture<Object> {
    private final Trigger trigger;
    private Runnable runnable;
    private final SimpleTriggerContext triggerContext;
    private final ScheduledExecutorService executor;
    private ScheduledFuture<?> currentFuture;
    private Date scheduledExecutionTime;
    private final Object triggerContextMonitor = new Object();

    public ReschedulingRunnable2(Runnable runnable, Trigger trigger, Clock clock, ScheduledExecutorService executor) {
        this.runnable = runnable;
        this.trigger = trigger;
        this.executor = executor;
        this.triggerContext = new SimpleTriggerContext(clock);
    }

    public ScheduledFuture<?> scheduled() {
        synchronized (this.triggerContextMonitor) {
            this.scheduledExecutionTime = this.trigger.nextExecutionTime(triggerContext);
            if (this.scheduledExecutionTime == null) {
                return null;
            }
            else {
                long initialDelay = this.scheduledExecutionTime.getTime() - this.triggerContext.getClock().millis();
                this.currentFuture = this.executor.schedule(this, initialDelay, TimeUnit.MILLISECONDS);
                return this;
            }
        }
    }

    private ScheduledFuture<?> obtainCurrentFuture() {
        Assert.state(this.currentFuture != null, "No scheduled future");
        return this.currentFuture;
    }

    @Override
    public void run() {
        Date actualExecutionTime = new Date(this.triggerContext.getClock().millis());
        this.runnable.run();
        Date completionTime = new Date(this.triggerContext.getClock().millis());
        synchronized (this.triggerContextMonitor) {
            Assert.state(this.scheduledExecutionTime != null, "No scheduled execution");
            this.triggerContext.update(this.scheduledExecutionTime, actualExecutionTime, completionTime);
            if (!this.obtainCurrentFuture().isCancelled()) {
                this.scheduled();
            }
        }
    }

    @Override
    public long getDelay(TimeUnit unit) {
        ScheduledFuture<?> curr;
        synchronized (this.triggerContextMonitor) {
            curr = this.obtainCurrentFuture();
        }
        return curr.getDelay(unit);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        synchronized (this.triggerContextMonitor) {
            return this.obtainCurrentFuture().cancel(mayInterruptIfRunning);
        }
    }

    @Override
    public boolean isCancelled() {
        synchronized (this.triggerContextMonitor) {
            return this.obtainCurrentFuture().isCancelled();
        }
    }

    @Override
    public boolean isDone() {
        synchronized (this.triggerContextMonitor) {
            return this.obtainCurrentFuture().isDone();
        }
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        ScheduledFuture<?> curr;
       synchronized (this.triggerContextMonitor) {
           curr = this.obtainCurrentFuture();
       }
       return curr.get();
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        ScheduledFuture<?> curr;
        synchronized (this.triggerContextMonitor) {
            curr = this.obtainCurrentFuture();
        }
        return curr.get(timeout, unit);
    }

    @Override
    public int compareTo(Delayed other) {
        if (this == other) {
            return 0;
        } else {
            long diff = this.getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS);
            return diff == 0L ? 0 : (diff < 0L ? -1 : 1);
        }
    }
}
