package com.example.plantcouponservice.vo;

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

    @NotNull
    private Integer memberNo;
}
