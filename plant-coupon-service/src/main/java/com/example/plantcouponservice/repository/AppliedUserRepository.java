package com.example.plantcouponservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AppliedUserRepository {
    private final RedisTemplate<String, String> redisTemplate;
    //set로 1인당 1개
    public Long add(Integer memberNo) {
        return redisTemplate.opsForSet()
                .add("applied_user", memberNo.toString());
    }

    public Boolean deleteAllAppliedUsers() {
        return redisTemplate.delete("applied_user");
    }
}
