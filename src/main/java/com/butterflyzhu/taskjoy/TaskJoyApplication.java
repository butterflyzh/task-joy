package com.butterflyzhu.taskjoy;

import com.butterflyzhu.taskjoy.annotation.EnableScheduling2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling2
public class TaskJoyApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskJoyApplication.class, args);
    }

}
