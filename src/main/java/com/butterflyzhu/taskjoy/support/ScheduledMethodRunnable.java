package com.butterflyzhu.taskjoy.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ScheduledMethodRunnable implements Runnable {

    private Method method;
    private Object bean;

    public ScheduledMethodRunnable(Method method, Object bean) {
        this.method = method;
        this.bean = bean;
    }

    @Override
    public void run() {
        try {
            method.invoke(bean);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
