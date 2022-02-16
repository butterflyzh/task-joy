package com.butterflyzhu.taskjoy.annotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

@Configuration(proxyBeanMethods = false)
@Role(2)
public class SchedulingConfiguration2 {

    @Bean(name = "com.butterflyzhu.taskjoy.annotation.ScheduledAnnotationBeanPostProcessor2")
    @Role(2)
    public ScheduledAnnotationBeanPostProcessor2 scheduledAnnotationBeanPostProcessor2() {
        return new ScheduledAnnotationBeanPostProcessor2();
    }
}
