package com.happychat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableAsync
@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class HappychatServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HappychatServerApplication.class, args);
    }

}
