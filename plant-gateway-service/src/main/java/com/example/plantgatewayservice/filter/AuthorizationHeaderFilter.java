package com.example.plantgatewayservice.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;


@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
    Environment env;
    public static class Config{
    }
    public AuthorizationHeaderFilter(Environment env) {
        super(Config.class);
        this.env = env;
    }
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return OnError(exchange, "로그인이 필요한 서비스입니다.", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer ", "");

            if (!isJwtValid(jwt)) {
                return OnError(exchange, "Jwt token is not valid", HttpStatus.UNAUTHORIZED);
            }

            // JWT에서 memberNo 추출
            String memberNo = extractMemberNoFromJwt(jwt);

            // 새 요청 객체 생성 및 memberNo 헤더 추가
            ServerHttpRequest newRequest = request.mutate()
                    .header("X-Member-No", memberNo)
                    .build();

            // 수정된 요청으로 교환 객체 업데이트
            return chain.filter(exchange.mutate().request(newRequest).build());
        });
    }

    private String extractMemberNoFromJwt(String jwt) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secretKey".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(jwt);

            // JWT 클레임에서 memberNo 추출
            return decodedJWT.getSubject();
        } catch (Exception e) {
            log.error("Failed to extract memberNo from JWT", e);
            return null;
        }
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;
        String memberNo = null;

        try {
            //복호화
            Algorithm algorithm = Algorithm.HMAC256("secretKey".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT decodedJWT = verifier.verify(jwt);
            memberNo = decodedJWT.getSubject();
        } catch (Exception e) {
            returnValue = false;
        }
        if (memberNo == null || memberNo.isEmpty()) {
            returnValue = false;
        }


        return returnValue;
    }
    //Mono, Flux => Spring WebFlux
    private Mono<Void> OnError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error(err);
        // 문자열로 에러 메시지 생성
        String errorMessage = err;
        byte[] bytes = errorMessage.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = new DefaultDataBufferFactory().wrap(bytes);

        // 컨텐트 타입 설정
        response.getHeaders().setContentType(MediaType.TEXT_PLAIN);

        // 에러 로깅
        log.error(err);

        // 에러 메시지를 응답 본문에 쓰기
        return response.writeWith(Flux.just(buffer));
    }

}
