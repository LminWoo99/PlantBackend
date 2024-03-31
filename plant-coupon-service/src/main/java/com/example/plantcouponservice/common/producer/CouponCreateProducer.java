package com.example.plantcouponservice.common.producer;

import com.example.plantcouponservice.vo.CouponRequestDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CouponCreateProducer {
    private final KafkaTemplate<String, CouponRequestDto> kafkaTemplate;

    public CouponCreateProducer(KafkaTemplate<String, CouponRequestDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void create(CouponRequestDto couponRequestDto) {
        kafkaTemplate.send("coupon_created", couponRequestDto);
    }

}
