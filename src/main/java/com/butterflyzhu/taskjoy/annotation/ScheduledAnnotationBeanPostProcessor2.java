package com.butterflyzhu.taskjoy.annotation;

import com.butterflyzhu.taskjoy.config.CronTaskAdapter;
import com.butterflyzhu.taskjoy.config.ScheduledTask2;
import com.butterflyzhu.taskjoy.config.ScheduledTaskRegistrar2;
import com.butterflyzhu.taskjoy.config.TaskStatus;
import com.butterflyzhu.taskjoy.support.ScheduledMethodRunnable;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

public class ScheduledAnnotationBeanPostProcessor2 implements BeanPostProcessor, SmartInitializingSingleton {
    private ScheduledTaskRegistrar2 registrar;

    public ScheduledAnnotationBeanPostProcessor2() {
        this.registrar = new ScheduledTaskRegistrar2();
    }

    @Override
    public void afterSingletonsInstantiated() {
        try {
            this.registrar.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Set<ScheduledTask2> getScheduledTasks() {
        return this.registrar.getScheduledTasks();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
        if (AnnotationUtils.isCandidateClass(targetClass, Scheduled2.class)) {
            Method[] declaredMethods = targetClass.getDeclaredMethods();
            Map<Method, Scheduled2> scheduledAnnotations = new HashMap<>();
            for (Method method : declaredMethods) {
                Scheduled2 scheduled2 = AnnotationUtils.findAnnotation(method, Scheduled2.class);
                if (scheduled2 != null) {
                    scheduledAnnotations.put(method, scheduled2);
                }
            }

            if (!scheduledAnnotations.isEmpty()) {
                scheduledAnnotations.forEach(((method, scheduled2) -> {
                    this.processScheduled(method, scheduled2, bean);
                }));
            }
        }
        return bean;
    }

    private void processScheduled(Method method, Scheduled2 scheduled2, Object bean) {
        try {
            Runnable runnable = this.createRunnable(method, bean);
            String cron = scheduled2.cron();
            long fixedDelay = scheduled2.fixedDelay();
            long fixedRate = scheduled2.fixedRate();
            if (StringUtils.hasText(cron)) {
                String zone = scheduled2.zone();
                TimeZone timeZone = null;
                if (!"-".equals(cron)) {
                    if (StringUtils.hasText(zone)) {
                        timeZone = StringUtils.parseTimeZoneString(zone);
                    }
                    else {
                        timeZone = TimeZone.getDefault();
                    }
                }
                CronTask cronTask = new CronTaskAdapter(runnable, new CronTrigger(cron, timeZone), scheduled2.value(), TaskStatus.NOT_START);
                this.registrar.addCronTask(cronTask);
            }
            else if (fixedDelay > 0L) {
                long initialDelay = covertToMillis(scheduled2.initialDelay(), scheduled2.timeUnit());
                fixedDelay = covertToMillis(scheduled2.fixedDelay(), scheduled2.timeUnit());
                // todo register
            }
            else if (fixedRate > 0L) {
                long initialDelay = covertToMillis(scheduled2.initialDelay(), scheduled2.timeUnit());
                fixedRate = covertToMillis(scheduled2.fixedRate(), scheduled2.timeUnit());
                // todo register
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Encountered invalid @Scheduled method '" + method.getName() + "': " + e.getMessage());
        }
    }

    private Runnable createRunnable(Method method, Object bean) {
        Assert.isTrue(method.getParameterCount() == 0, "Only no-arg methods may be annotated with @Scheduled");
        if (Modifier.isPublic(method.getModifiers()) || Modifier.isProtected(method.getModifiers())) {
            return new ScheduledMethodRunnable(method, bean);
        }
        else {
            throw new IllegalArgumentException(String.format("Need to invoke method '%s' declared on target class '%s', but not found in target type.", method.getName(), method.getDeclaringClass().getSimpleName()));
        }
    }

    private static long covertToMillis(long value, TimeUnit timeUnit) {
        return TimeUnit.MILLISECONDS.convert(value, timeUnit);
    }

}
