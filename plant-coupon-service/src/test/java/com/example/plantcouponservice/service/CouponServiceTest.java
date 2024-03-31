package com.example.plantcouponservice.service;

import com.example.plantcouponservice.repository.CouponRepository;
import com.example.plantcouponservice.vo.CouponRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CouponServiceTest {
    @Autowired
    CouponService couponService;

    @Autowired
    CouponRepository couponRepository;

    @Test
    void applyCouponTest() throws InterruptedException {
        //given
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        //when
        for (int i = 0; i<threadCount; i++) {
            CouponRequestDto couponRequestDto = new CouponRequestDto(i, 1L, 2000);
            executorService.submit(() -> {
                try{
                    couponService.applyCoupon(couponRequestDto);
                }
                finally {
                    countDownLatch.countDown();

                }
            });
        }
        countDownLatch.await();
        long count = couponRepository.count();
        assertThat(count).isEqualTo(100);
    }

    @Test
    void getCoupon() {
    }

    @Test
    void useCoupon() {
    }
}