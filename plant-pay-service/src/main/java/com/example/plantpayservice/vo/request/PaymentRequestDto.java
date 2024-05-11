package com.example.plantpayservice.vo.request;

import com.example.plantpayservice.domain.entity.CouponStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {
    @NotNull
    private Integer payMoney;
    private Integer discountPrice;

    @NotNull
    private Integer memberNo;
    private Long couponNo;
    private Integer sellerNo;
    private CouponStatus couponStatus;

    public PaymentRequestDto(@NotNull Integer payMoney, @NotNull Integer memberNo) {
        this.payMoney = payMoney;
        this.memberNo = memberNo;
    }
}
