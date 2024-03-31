package com.example.plantcouponservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponRequestDto {
    private Integer memberNo;
    private Long tradeBoardNo;
    private Integer discountPrice;

}
