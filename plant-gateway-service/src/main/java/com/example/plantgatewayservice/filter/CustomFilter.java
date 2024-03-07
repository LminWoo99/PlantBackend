package com.example.plantgatewayservice.filter;

import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {
    public CustomFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpResponse response = exchange.getResponse();

            // Response 헤더 설정을 커밋하기 전에 조작
            response.beforeCommit(() -> {
                // 기존 Connection 헤더 제거
                response.getHeaders().remove("Connection");
                // Connection 헤더를 keep-alive로 설정
                response.getHeaders().set("Connection", "keep-alive");
                return Mono.empty();
            });

            return chain.filter(exchange);
        };
    }
    @Data

    public static class Config {
        // Config 클래스 구현이 필요하다면 여기에 추가
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
