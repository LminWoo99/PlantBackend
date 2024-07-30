package com.example.plantpayservice.vo.response;

import com.example.plantpayservice.domain.entity.Payment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.kafka.common.metrics.Stat;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // DTO 를 JSON으로 변환 시 null값인 field 제외
public class StatusResponseDto {
    private Integer status;
    private Object data;

    public StatusResponseDto(Integer status) {
        this.status = status;
    }

    public static StatusResponseDto addStatus(Integer status) {
        return new StatusResponseDto(status);
    }
    public static StatusResponseDto addStatus(Integer status, Payment data) {
        return new StatusResponseDto(status, data);
    }

    public static StatusResponseDto success(){
        return new StatusResponseDto(200);
    }
    public static StatusResponseDto failedIdempotency() {
        return new StatusResponseDto(409);
    }


}
