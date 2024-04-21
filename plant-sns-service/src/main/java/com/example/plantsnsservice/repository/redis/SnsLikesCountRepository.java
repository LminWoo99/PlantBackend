package com.example.plantsnsservice.repository.redis;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SnsLikesCountRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public Long increment(Integer memberNo, Long postId) {
        // 좋아요 추가: memberNo와 postId 조합을 키로 사용
        String key = "sns_likes:" + postId;
        String value = memberNo.toString();
        return redisTemplate.opsForSet().add(key, value);
    }

    public Long decrement(Integer memberNo, Long postId) {
        // 좋아요 제거: 특정 postId에 대해 특정 memberNo를 제거
        String key = "sns_likes:" + postId;
        String value = memberNo.toString();
        return redisTemplate.opsForSet().remove(key, value);
    }
    public boolean isMember(String key, Integer memberNo) {
        return redisTemplate.opsForSet().isMember(key, memberNo.toString());
    }
}
