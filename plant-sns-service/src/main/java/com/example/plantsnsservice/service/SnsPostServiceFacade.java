package com.example.plantsnsservice.service;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SnsPostServiceFacade {
    private final SnsPostService snsPostService;
    private final RedissonClient redissonClient;

    /**
     * 게시글 좋아요 증가 메서드 락을 걸기 위한 메서드
     * redis의 분산락을 통한 동시성 제어
     * 실제락을 거는 시점은 퍼사드 패턴으로 분리하여
     * @Transactional 바깥에서 락을 걸어줌
     * @param : Long id(게시글 번호)
     */
    public void updateSnsLikesCountLock(Long id) {
        final String lockName = "likes:lock";
        final RLock lock = redissonClient.getLock(lockName);

        try {
            if (!lock.tryLock(10, 1, TimeUnit.SECONDS))
                return;
            snsPostService.updateSnsLikesCountUseRedisson(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (lock != null && lock.isLocked()) {
                lock.unlock();
            }
        }
    }
}
