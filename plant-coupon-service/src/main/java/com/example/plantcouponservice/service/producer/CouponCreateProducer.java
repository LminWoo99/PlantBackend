package com.example.plantcouponservice.service.producer;

import com.example.plantcouponservice.vo.request.CouponRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponCreateProducer {
    private final KafkaTemplate<String, CouponRequestDto> kafkaTemplate;


    public void create(CouponRequestDto couponRequestDto) {
        kafkaTemplate.send("coupon_created", couponRequestDto);
    }

}
