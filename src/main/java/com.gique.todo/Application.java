package com.gique.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void init() {
        //log.warn("Memory: Max[{}], Free[{}], Total[{}]", Runtime.getRuntime().maxMemory(),
                //Runtime.getRuntime().freeMemory(), Runtime.getRuntime().totalMemory());
    }
}
