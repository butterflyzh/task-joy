package com.butterflyzhu.taskjoy.config;

import org.springframework.scheduling.config.Task;

public interface TaskInfo {

    String getTaskName();

    TaskStatus getTaskStatus();
}
