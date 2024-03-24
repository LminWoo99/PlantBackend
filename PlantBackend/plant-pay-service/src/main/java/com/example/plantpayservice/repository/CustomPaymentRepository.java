package com.example.plantpayservice.repository;

import com.example.plantpayservice.domain.entity.Payment;
import com.example.plantpayservice.vo.request.UpdatePaymentRequestDto;

public interface CustomPaymentRepository {
    void updatePayMoney(UpdatePaymentRequestDto updatePaymentRequestDto);

    void tradePayMoney(Payment sellerPayment, Payment buyerPayment);
}
