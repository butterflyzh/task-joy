package com.butterflyzhu.taskjoy.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({SchedulingConfiguration2.class})
@Documented
public @interface EnableScheduling2 {
}
