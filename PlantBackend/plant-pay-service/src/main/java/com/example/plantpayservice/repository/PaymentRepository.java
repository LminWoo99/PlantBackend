package com.example.plantpayservice.repository;

import com.example.plantpayservice.domain.entity.Payment;
import com.example.plantpayservice.vo.request.PaymentRequestDto;
import com.example.plantpayservice.vo.request.UpdatePaymentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> , CustomPaymentRepository{
    Payment findByMemberNo(Integer memberNo);

    void tradePayMoney(Payment sellerPayment, Payment buyerPayment);
}
