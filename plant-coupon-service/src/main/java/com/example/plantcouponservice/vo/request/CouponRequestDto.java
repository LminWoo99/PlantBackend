package com.example.plantcouponservice.vo.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponRequestDto implements Serializable {
    private Integer memberNo;
    private Integer discountPrice;

}
