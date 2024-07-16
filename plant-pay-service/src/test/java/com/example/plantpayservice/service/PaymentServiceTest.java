package com.example.plantpayservice.service;

import com.example.plantpayservice.domain.entity.Payment;
import com.example.plantpayservice.exception.CustomException;
import com.example.plantpayservice.repository.PaymentRepository;
import com.example.plantpayservice.vo.request.PaymentRequestDto;
import com.example.plantpayservice.vo.response.PaymentResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//service&repository 통합 테스트
@SpringBootTest
class PaymentServiceTest {
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    PaymentService paymentService;
    @AfterEach
    public void init() {
        paymentRepository.deleteAll();
    }
    @Test
    @DisplayName("식구페이 머니 충전 테스트")
    void chargePayMoneyTest() {
        //given
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto(12000, 1);

        //when
        paymentService.chargePayMoney(paymentRequestDto);
        //then
        Payment payment = paymentRepository.findByMemberNo(1);
        assertThat(payment.getPayMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("식구페이 머니 환불 메서드 동시성 제어 테스트")
    void refundPayMoneyConcurrencyTest() throws InterruptedException {
        //given
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto(12000, 10);


        paymentService.chargePayMoney(paymentRequestDto);
        PaymentRequestDto paymentRequestDto1 = new PaymentRequestDto(1, 10);
        int threadCount = 80;
        //비동기로 실행되는 작업을 단순화해서 사용하게 도와주는 자바의 api
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        //카운트다운래치는 다른 스래드에서 수행중인 작업을 완료될때까지 대기하도록 도와주는 클래스
        CountDownLatch latch = new CountDownLatch(threadCount);
        //when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    paymentService.refundPayMoney(paymentRequestDto1);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        //then
        PaymentResponseDto paymentResponseDto = paymentService.getPayMoney(10);
        //-2면 안되고 5999이여함
        assertThat(paymentResponseDto.getPayMoney()).isEqualTo(11920);


    }

    @Test
    @DisplayName("보유 페이머니 조회 테스트")
    void getPayMoneyTest() {
        //given
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto(12000, 1);
        paymentService.chargePayMoney(paymentRequestDto);

        //when
        PaymentResponseDto paymentResponseDto = paymentService.getPayMoney(paymentRequestDto.getMemberNo());
        //then
        assertThat(paymentResponseDto.getPayMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("페이머니로 거래 테스트")
    void tradePayMoneyTest() throws JsonProcessingException {
        //given
        PaymentRequestDto buyerRequestDto = new PaymentRequestDto(12000, 1);
        paymentService.chargePayMoney(buyerRequestDto);

        PaymentRequestDto sellerRequestDto = new PaymentRequestDto(6000, 2);
        paymentService.chargePayMoney(sellerRequestDto);

        PaymentRequestDto amountDto = new PaymentRequestDto(2000, 1);
        amountDto.setSellerNo(2);
        //when
        paymentService.tradePayMoney(amountDto);
        //then
        assertThat(paymentService.getPayMoney(1).getPayMoney()).isEqualTo(10000);
        assertThat(paymentService.getPayMoney(2).getPayMoney()).isEqualTo(8000);

    }
    @Test
    @DisplayName("구매자 보유 페이머니가 거래할 식물 가격보다 적을 때 예외테스트")
    void tradePayMoneyExceptionTest() throws Exception{
        //given
        PaymentRequestDto buyerRequestDto = new PaymentRequestDto(1000, 1);
        paymentService.chargePayMoney(buyerRequestDto);

        PaymentRequestDto sellerRequestDto = new PaymentRequestDto(6000, 2);
        paymentService.chargePayMoney(sellerRequestDto);

        PaymentRequestDto amountDto = new PaymentRequestDto(2000, 1);
        amountDto.setSellerNo(2);
        //when
        //then
        assertThrows(CustomException.class, () -> paymentService.tradePayMoney(amountDto));

    }
}