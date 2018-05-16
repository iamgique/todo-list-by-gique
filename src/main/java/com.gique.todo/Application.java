package com.gique.todo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;


@SpringBootApplication
@EnableAutoConfiguration
public class Application {
    private static final Logger log = LogManager.getLogger(Application.class);

    @PostConstruct
    public void init() {
        log.warn("Memory: Max[{}], Free[{}], Total[{}]", Runtime.getRuntime().maxMemory(),
                Runtime.getRuntime().freeMemory(), Runtime.getRuntime().totalMemory());
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
