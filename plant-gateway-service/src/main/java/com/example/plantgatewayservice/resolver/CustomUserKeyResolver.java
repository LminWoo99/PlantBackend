package com.example.plantgatewayservice.resolver;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Configuration
public class CustomUserKeyResolver {
    @Bean
    KeyResolver userKeyResolver() {
        return exchange -> {
            String memberNo = exchange.getRequest().getHeaders().getFirst("X-Member-No");
            return Mono.just(memberNo);
        };
    }
}

