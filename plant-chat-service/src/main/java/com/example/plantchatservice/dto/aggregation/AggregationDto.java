package com.example.plantchatservice.dto.aggregation;

import lombok.*;

import java.io.Serializable;
@Getter @ToString @Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AggregationDto implements Serializable {

    private Long tradeBoardNo;
    private String isIncrease;
    private AggregationTarget target;
}
