package com.example.plantpayservice.repository;

import com.example.plantpayservice.domain.entity.Payment;
import com.example.plantpayservice.vo.request.PaymentRequestDto;
import com.example.plantpayservice.vo.request.UpdatePaymentRequestDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import static com.example.plantpayservice.domain.entity.QPayment.payment;

@RequiredArgsConstructor
public class PaymentRepositoryImpl implements CustomPaymentRepository{
    private final JPAQueryFactory jpaQueryFactory;

    public void updatePayMoney(UpdatePaymentRequestDto updatePaymentRequestDto) {
        jpaQueryFactory.update(payment)
                .set(payment.payMoney, (updatePaymentRequestDto.getPayMoney()))
                .where(payment.memberNo.eq(updatePaymentRequestDto.getMemberNo()))
                .execute();
    }
    public void tradePayMoney(Payment sellerPayment, Payment buyerPayment) {
        jpaQueryFactory.update(payment)
                .set(payment.payMoney, (sellerPayment.getPayMoney()))
                .where(payment.memberNo.eq(sellerPayment.getMemberNo()))
                .execute();
        jpaQueryFactory.update(payment)
                .set(payment.payMoney, (buyerPayment.getPayMoney()))
                .where(payment.memberNo.eq(buyerPayment.getMemberNo()))
                .execute();
    }
}
