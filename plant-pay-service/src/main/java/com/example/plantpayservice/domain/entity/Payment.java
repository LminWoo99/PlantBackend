package com.example.plantpayservice.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "PAYMENT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_no")
    private Long paymentNo;
    @Column(name = "pay_money")
    private Integer payMoney;

    @Column(name = "member_no")
    private Integer memberNo;
    @Builder
    public Payment(Long paymentNo, Integer payMoney, Integer memberNo) {
        this.paymentNo = paymentNo;
        this.payMoney = payMoney;
        this.memberNo= memberNo;
    }
    public void decreasePayMoney(Integer payMoney) {
        this.payMoney -= payMoney;
    }
    public void increasePayMoney(Integer payMoney) {
        this.payMoney += payMoney;
    }
}
