package com.example.plantcouponservice;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableEurekaClient
@EnableScheduling
@EnableBatchProcessing
@EnableWebMvc
public class PlantCouponServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlantCouponServiceApplication.class, args);
    }

}
