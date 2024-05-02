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
                return OnError(exchange, "Jwt token is not vaild", HttpStatus.UNAUTHORIZED);
            }
            return chain.filter(exchange);

        });
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;
        String username = null;

        try {
            //복호화
            Algorithm algorithm = Algorithm.HMAC256("secretKey".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT decodedJWT = verifier.verify(jwt);
            username = decodedJWT.getSubject();
            String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
//            username = Jwts.parser().setSigningKey(env.getProperty("token.secret"))
//                    .parseClaimsJws(jwt).getBody()
//                    .getSubject();
        } catch (Exception e) {
            returnValue = false;
        }
        if (username == null || username.isEmpty()) {
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
