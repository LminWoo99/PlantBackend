package com.example.plantcouponservice.domain;


import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@NoArgsConstructor
public class FailedEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer memberNo;

    public FailedEvent(Integer memberNo) {
        this.memberNo = memberNo;
    }
}
