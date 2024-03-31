package com.example.plantpayservice.repository;

import com.example.plantpayservice.domain.entity.Payment;
import com.example.plantpayservice.vo.request.PaymentRequestDto;

public interface CustomPaymentRepository {

    void tradePayMoney(Integer sellerNo, Integer buyerNo, PaymentRequestDto paymentRequestDto);

    void existsByMemberNoUpdatePayMoney(PaymentRequestDto paymentRequestDto);
}
