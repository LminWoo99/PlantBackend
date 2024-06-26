package com.example.plantchatservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableJpaAuditing
@EnableWebMvc
public class PlantChatServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlantChatServiceApplication.class, args);
    }

}
