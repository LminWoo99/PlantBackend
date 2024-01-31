package com.example.plantconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class PlantConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlantConfigServerApplication.class, args);
    }

}
