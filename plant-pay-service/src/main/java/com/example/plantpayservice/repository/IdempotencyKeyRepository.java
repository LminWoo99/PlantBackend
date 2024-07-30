package com.example.plantpayservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class IdempotencyKeyRepository {
    private final RedisTemplate<String, String> redisTemplate;

    private static final long EXPIRATION_TIME = 10; // 10초
    /**
     * 결제 요청 멱등성 보장
     * @param : String key(메서드키-유저id-금액)
     * @return True | False
     */
    public boolean addRequest(String key) {
        return redisTemplate.opsForValue().setIfAbsent(key, "success", EXPIRATION_TIME, TimeUnit.SECONDS);
    }
}
