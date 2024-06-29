package com.example.plantpayservice.repository;

import com.example.plantpayservice.domain.entity.Payment;
import com.example.plantpayservice.vo.request.PaymentRequestDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;

import static com.example.plantpayservice.domain.entity.QPayment.payment;

@RequiredArgsConstructor
public class PaymentRepositoryImpl implements CustomPaymentRepository{
    private final JPAQueryFactory jpaQueryFactory;
    //벌크연산 수행후 데이터 정합성 문제가 발생할 수있으므로 영속성 컨텍스트에 반영되지 않는 문제점 해결
    private final EntityManager em;
    public void existsByMemberNoUpdatePayMoney(PaymentRequestDto paymentRequestDto) {
        jpaQueryFactory.update(payment)
                .set(payment.payMoney, payment.payMoney.add(paymentRequestDto.getPayMoney()))
                .where(payment.memberNo.eq(paymentRequestDto.getMemberNo()))
                .execute();
        em.flush();
        em.clear();


    }
    public void tradePayMoney(Integer sellerNo, Integer buyerNo, PaymentRequestDto paymentRequestDto, Integer buyerPayMoney) {
        jpaQueryFactory.update(payment)
                .set(payment.payMoney, payment.payMoney.add(buyerPayMoney))
                .where(payment.memberNo.eq(sellerNo))
                .execute();
        jpaQueryFactory.update(payment)
                .set(payment.payMoney, payment.payMoney.subtract(paymentRequestDto.getPayMoney()))
                .where(payment.memberNo.eq(buyerNo))
                .execute();
        em.flush();
        em.clear();

    }
}
