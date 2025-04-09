package com.rangelmrk.homehelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HomeHelperApp {
    public static void main(String[] args) {
        SpringApplication.run(HomeHelperApp.class, args);
    }
}