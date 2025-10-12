package com.example.msevent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.msevent.client")
public class MsEventApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsEventApplication.class, args);
    }

}
