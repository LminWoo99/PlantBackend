package com.example.plantpayservice.service;

import com.example.plantpayservice.domain.entity.Payment;
import com.example.plantpayservice.repository.IdempotencyKeyRepository;
import com.example.plantpayservice.repository.PaymentRepository;
import com.example.plantpayservice.vo.request.PaymentRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest

class PaymentServiceIdempotencyTest {
    @Autowired
    PaymentService paymentService;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    IdempotencyKeyRepository idempotencyKeyRepository;
    @Test
    @DisplayName("충전 요청의 멱등성 테스트")
    void chargePayMoney() throws InterruptedException {
        // given
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // when
        for (int i = 0; i < threadCount; i++) {
            PaymentRequestDto paymentRequestDto = new PaymentRequestDto(10000, 1);
            executorService.submit(() -> {
                try {
                    paymentService.chargePayMoney(paymentRequestDto, "charge-1-10000");
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        // then
        Payment payment = paymentRepository.findByMemberNo(1);
        assertThat(payment.getPayMoney()).isEqualTo(10000);
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(threadCount - 1);

        // 멱등성 키가 저장되었는지 확인
        assertTrue(idempotencyKeyRepository.addRequest("charge-1-10000"));

        // 테스트 후 정리
        paymentRepository.delete(payment);
    }


    @Test
    @DisplayName("환불 요청의 멱등성 테스트")
    void refundPayMoney() throws InterruptedException {
        // given
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // 초기 잔액 설정
        Payment initialPayment = new Payment(20000, 1);

        paymentRepository.save(initialPayment);
        // when
        for (int i = 0; i < threadCount; i++) {
            PaymentRequestDto paymentRequestDto = new PaymentRequestDto(10000, 1);
            executorService.submit(() -> {
                try {
                    paymentService.refundPayMoney(paymentRequestDto, "refund-1-10000");
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        // then
        Payment payment = paymentRepository.findByMemberNo(1);
        assertThat(payment.getPayMoney()).isEqualTo(10000);
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(threadCount - 1);

        // 멱등성 키가 저장되었는지 확인
        assertTrue(!idempotencyKeyRepository.addRequest("refund-1-10000"));
        // 테스트 후 정리
        paymentRepository.delete(payment);
    }

    @Test
    @DisplayName("거래 요청의 멱등성 테스트")
    void tradePayMoney() throws InterruptedException {
        // given
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // 초기 잔액 설정 (구매자와 판매자)
        Payment buyerPayment = new Payment(20000, 1);
        paymentRepository.save(buyerPayment);

        Payment sellerPayment = new Payment(0, 2);
        paymentRepository.save(sellerPayment);

        // when
        for (int i = 0; i < threadCount; i++) {
            PaymentRequestDto paymentRequestDto = new PaymentRequestDto(10000, 1);
            paymentRequestDto.setSellerNo(2);
            paymentRequestDto.setCouponNo(1L);
            executorService.submit(() -> {
                try {
                    paymentService.tradePayMoney(paymentRequestDto, "trade-1-10000");
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        // then
        Payment updatedBuyerPayment = paymentRepository.findByMemberNo(1);
        Payment updatedSellerPayment = paymentRepository.findByMemberNo(2);
        assertThat(updatedBuyerPayment.getPayMoney()).isEqualTo(10000);
        assertThat(updatedSellerPayment.getPayMoney()).isEqualTo(10000);
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(threadCount - 1);

        // 멱등성 키가 저장되었는지 확인
        assertTrue(!idempotencyKeyRepository.addRequest("trade-1-10000"));

        // 테스트 후 정리
        paymentRepository.delete(updatedBuyerPayment);
        paymentRepository.delete(updatedSellerPayment);
    }
}