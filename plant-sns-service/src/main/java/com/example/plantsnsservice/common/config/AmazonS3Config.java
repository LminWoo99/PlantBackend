package com.example.plantsnsservice.common.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.example.plantsnsservice.common.properties.AwsS3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AmazonS3Config {

    private final AwsS3Properties properties;

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(properties.getRegion())
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(properties.getAccessKey(), properties.getSecretKey())))
                .build();
    }


}