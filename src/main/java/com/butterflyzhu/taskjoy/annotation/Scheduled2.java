package com.butterflyzhu.taskjoy.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scheduled2 {

    String value() default "";

    String cron() default "";

    String zone() default "";

    long fixedRate() default -1L;

    long fixedDelay() default -1L;

    long initialDelay() default -1L;

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
