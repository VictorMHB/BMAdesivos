package com.github.victormhb.bmadesivos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BMAdesivosApplication {

    public static void main(String[] args) {
        SpringApplication.run(BMAdesivosApplication.class, args);
    }

}
