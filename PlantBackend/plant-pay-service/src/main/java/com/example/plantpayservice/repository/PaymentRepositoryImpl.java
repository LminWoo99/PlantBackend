package com.example.plantpayservice.repository;

import com.example.plantpayservice.domain.entity.Payment;
import com.example.plantpayservice.vo.request.PaymentRequestDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import static com.example.plantpayservice.domain.entity.QPayment.payment;

@RequiredArgsConstructor
public class PaymentRepositoryImpl implements CustomPaymentRepository{
    private final JPAQueryFactory jpaQueryFactory;

    public void updatePayMoney(PaymentRequestDto paymentRequestDto) {
        jpaQueryFactory.update(payment)
                .set(payment.payMoney, payment.payMoney.subtract(paymentRequestDto.getPayMoney()))
                .where(payment.memberNo.eq(paymentRequestDto.getMemberNo()))
                .execute();
    }

    public void existsByMemberNoUpdatePayMoney(PaymentRequestDto paymentRequestDto) {
        jpaQueryFactory.update(payment)
                .set(payment.payMoney, payment.payMoney.add(paymentRequestDto.getPayMoney()))
                .where(payment.memberNo.eq(paymentRequestDto.getMemberNo()))
                .execute();

    }
    public void tradePayMoney(Integer sellerNo, Integer buyerNo, PaymentRequestDto paymentRequestDto) {
        jpaQueryFactory.update(payment)
                .set(payment.payMoney, payment.payMoney.add(paymentRequestDto.getPayMoney()))
                .where(payment.memberNo.eq(sellerNo))
                .execute();
        jpaQueryFactory.update(payment)
                .set(payment.payMoney, payment.payMoney.subtract(paymentRequestDto.getPayMoney()))
                .where(payment.memberNo.eq(buyerNo))
                .execute();
    }
}
