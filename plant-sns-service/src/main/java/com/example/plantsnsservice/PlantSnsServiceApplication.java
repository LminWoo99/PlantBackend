package com.example.plantsnsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableEurekaClient
@EnableWebMvc
@EnableJpaAuditing
public class PlantSnsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlantSnsServiceApplication.class, args);
    }

}
