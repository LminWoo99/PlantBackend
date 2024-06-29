package com.example.plantcouponservice.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Getter
@NoArgsConstructor
public class FailedEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer memberNo;
    private Integer discountPrice;

    public FailedEvent(Integer memberNo, Integer discountPrice) {
        this.memberNo = memberNo;
        this.discountPrice = discountPrice;
    }
}
