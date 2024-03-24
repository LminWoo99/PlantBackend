package com.example.plantpayservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePaymentRequestDto {
    @NotNull
    private Integer payMoney;

    @NotNull
    private Integer refundPayMoney;

    @NotNull
    private Integer memberNo;
}
