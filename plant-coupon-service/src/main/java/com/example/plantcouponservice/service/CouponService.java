package com.example.plantcouponservice.service;

import com.example.plantcouponservice.common.exception.CouponNotFoundException;
import com.example.plantcouponservice.domain.Coupon;
import com.example.plantcouponservice.repository.CouponCountRepository;
import com.example.plantcouponservice.repository.CouponRepository;
import com.example.plantcouponservice.vo.CouponRequestDto;
import com.example.plantcouponservice.vo.CouponResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;

    /**
     * 쿠폰 발급
     * 요구사항 정리 : 하루에 중고 거래시 할인받을수 있는 100개 쿠폰을 발급
     * 쿠폰은 다음날 자정에 일괄 삭제
     * 100개 넘으면 발급 불가
     * @param : CouponRequestDto couponRequestDto
     */
    public void applyCoupon(CouponRequestDto couponRequestDto) {
        Long count = couponCountRepository.increment();
        //오늘 날짜 기준으로 100개보다 많으면 return
        if (count > 100) {
            return;
        }
        Coupon coupon=Coupon.builder()
                        .memberNo(couponRequestDto.getMemberNo())
                        .tradeBoardNo(couponRequestDto.getTradeBoardNo())
                        .discountPrice(couponRequestDto.getDiscountPrice())
                        .regDate(LocalDateTime.now())
                        .build();

        couponRepository.save(coupon);
    }

    /**
     * 본인 쿠폰 조회
     * @param : Integer memberNo
     */
    @Transactional(readOnly = true)
    public CouponResponseDto getCoupon(Integer memberNo) {
        Coupon coupon = couponRepository.findByMemberNo(memberNo);
        if (coupon == null) {
            log.info("쿠폰이 존재하지 않습니다");
            throw new CouponNotFoundException("쿠폰이 존재하지 않습니다");
        }
        return CouponResponseDto.builder()
                .tradeBoardNo(coupon.getTradeBoardNo())
                .discountPrice(coupon.getDiscountPrice())
                .build();
    }

    /**
     * 쿠폰 사용 메서드
     * @param : Integer memberNo
     */
    @Transactional
    public void useCoupon(Integer memberNo) {
        Coupon coupon = couponRepository.findByMemberNo(memberNo);
        //사용완료
        coupon.useCoupon();

        couponRepository.save(coupon);
    }
    /**
     * 쿠폰 일괄 삭제 메서드
     * @param : Integer memberNo
     */

}
