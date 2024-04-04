package com.example.plantcouponservice.common.consumer;


import com.example.plantcouponservice.domain.Coupon;
import com.example.plantcouponservice.domain.FailedEvent;
import com.example.plantcouponservice.repository.CouponRepository;
import com.example.plantcouponservice.repository.FailedEventRepository;
import com.example.plantcouponservice.vo.CouponRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
public class CouponCreatedConsumer {
    private final CouponRepository couponRepository;
    private final FailedEventRepository failedEventRepository;
    private final Logger logger = LoggerFactory.getLogger(CouponCreatedConsumer.class);


    @KafkaListener(topics = "coupon_created", groupId = "coupon")
    public void listener(CouponRequestDto couponRequestDto) {

        try{
            Coupon coupon=Coupon.builder()
                    .memberNo(couponRequestDto.getMemberNo())
                    .discountPrice(couponRequestDto.getDiscountPrice())
                    .regDate(LocalDateTime.now())
                    .build();
            couponRepository.save(coupon);
        }
        catch (Exception e){
            logger.error("Failed to create coupon: "+ couponRequestDto.getMemberNo());
            failedEventRepository.save(new FailedEvent(couponRequestDto.getMemberNo()));
        }
    }
}
