package com.butterflyzhu.taskjoy.config;

import com.butterflyzhu.taskjoy.concurrent.SimpleTaskScheduler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.FixedDelayTask;
import org.springframework.scheduling.config.FixedRateTask;
import org.springframework.scheduling.config.Task;

import java.util.*;
import java.util.concurrent.Executors;

public class ScheduledTaskRegistrar2 implements InitializingBean {

    private TaskScheduler taskScheduler;

    private List<CronTask> cronTasks;
    private List<FixedDelayTask> fixedDelayTasks;
    private List<FixedRateTask> fixedRateTasks;
    private final Map<Task, ScheduledTask2> unresolvedTasks = new HashMap<>(16);
    private final Set<ScheduledTask2> scheduledTasks = new HashSet<>();

    public void setTaskScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.scheduleTasks();
    }

    protected void scheduleTasks() {
        if (this.taskScheduler == null) {
            this.taskScheduler = new SimpleTaskScheduler(Executors.newSingleThreadScheduledExecutor());
        }

        if (this.cronTasks != null) {
            for (CronTask task : this.cronTasks) {
                this.addScheduledTask(this.scheduledCronTask(task));
            }
        }

        if (this.fixedDelayTasks != null) {
            for (FixedDelayTask task : this.fixedDelayTasks) {
                this.addScheduledTask(this.scheduledFixedDelayTask(task));
            }
        }

        if (this.fixedRateTasks != null) {
            for (FixedRateTask task : this.fixedRateTasks) {
                this.addScheduledTask(this.scheduledFixedRateTask(task));
            }
        }
    }

    public ScheduledTask2 scheduledCronTask(CronTask task) {
        ScheduledTask2 scheduledTask = this.unresolvedTasks.remove(task);
        boolean newTask = false;
        if (scheduledTask == null) {
            newTask = true;
            scheduledTask = new ScheduledTask2(task);
        }

        if (this.taskScheduler !=  null) {
            scheduledTask.future = this.taskScheduler.schedule(task.getRunnable(), task.getTrigger());
        }
        else {
            this.addCronTask(task);
            this.unresolvedTasks.put(task, scheduledTask);
        }

        return newTask ? scheduledTask : null;
    }

    public ScheduledTask2 scheduledFixedDelayTask(FixedDelayTask task) {
        ScheduledTask2 scheduledTask = this.unresolvedTasks.remove(task);
        boolean newTask = false;
        if (scheduledTask == null) {
            newTask = true;
            scheduledTask = new ScheduledTask2(task);
        }

        if (this.taskScheduler != null) {
            if (task.getInitialDelay() > 0L) {
                Date startTime = new Date(this.taskScheduler.getClock().millis() + task.getInitialDelay());
                scheduledTask.future = this.taskScheduler.scheduleWithFixedDelay(task.getRunnable(), startTime, task.getInterval());
            }
            else {
                scheduledTask.future = this.taskScheduler.scheduleWithFixedDelay(task.getRunnable(), task.getInterval());
            }
        }
        else {
            this.addFixedDelayTask(task);
            this.unresolvedTasks.put(task, scheduledTask);
        }
        return newTask ? scheduledTask : null;
    }

    public ScheduledTask2 scheduledFixedRateTask(FixedRateTask task) {
        ScheduledTask2 scheduledTask = this.unresolvedTasks.remove(task);
        boolean newTask = false;
        if (scheduledTask == null) {
            newTask = true;
            scheduledTask = new ScheduledTask2(task);
        }

        if (this.taskScheduler != null) {
            if (task.getInitialDelay() > 0L) {
                Date startTime = new Date(this.taskScheduler.getClock().millis() + task.getInitialDelay());
                scheduledTask.future = this.taskScheduler.scheduleAtFixedRate(task.getRunnable(), startTime, task.getInterval());
            }
            else {
                scheduledTask.future = this.taskScheduler.scheduleAtFixedRate(task.getRunnable(), task.getInterval());
            }
        }
        else {
            this.addFixedRateTask(task);
            this.unresolvedTasks.put(task, scheduledTask);
        }
        return newTask ? scheduledTask : null;
    }

    public void addCronTask(CronTask task) {
        if (this.cronTasks == null) {
            this.cronTasks = new ArrayList<>();
        }
        this.cronTasks.add(task);
    }

    public void addFixedDelayTask(FixedDelayTask task) {
        if (this.fixedDelayTasks == null) {
            this.fixedDelayTasks = new ArrayList<>();
        }
        this.fixedDelayTasks.add(task);
    }

    public void addFixedRateTask(FixedRateTask task) {
        if (this.fixedRateTasks == null) {
            this.fixedRateTasks = new ArrayList<>();
        }
        this.fixedRateTasks.add(task);
    }

    private void addScheduledTask(ScheduledTask2 task) {
        if (task != null) {
            this.scheduledTasks.add(task);
        }
    }

    public Set<ScheduledTask2> getScheduledTasks() {
        return new HashSet<>(this.scheduledTasks);
    }
}
