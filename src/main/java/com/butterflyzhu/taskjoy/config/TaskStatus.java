package com.butterflyzhu.taskjoy.config;

public enum TaskStatus {
    NOT_START("未开始"),
    RUNNING("进行中"),
    INTERRUPT("中断"),
    CANCEL("取消"),
    FINISH("完成");

    private String name;

    TaskStatus(String name) {
        this.name = name;
    }
}
