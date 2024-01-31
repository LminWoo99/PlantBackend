package com.example.plantchatservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class PlantChatServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlantChatServiceApplication.class, args);
    }

}
