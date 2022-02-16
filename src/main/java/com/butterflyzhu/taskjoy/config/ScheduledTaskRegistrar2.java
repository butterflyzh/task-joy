package com.butterflyzhu.taskjoy.config;

import com.butterflyzhu.taskjoy.concurrent.SimpleTaskScheduler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.Task;

import java.util.*;
import java.util.concurrent.Executors;

public class ScheduledTaskRegistrar2 implements InitializingBean {

    private TaskScheduler taskScheduler;

    private List<CronTask> cronTasks;
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
            for (CronTask task : cronTasks) {
                this.addScheduledTask(this.scheduledCronTask(task));
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

    public void addCronTask(CronTask task) {
        if (this.cronTasks == null) {
            this.cronTasks = new ArrayList<>();
        }
        this.cronTasks.add(task);
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
