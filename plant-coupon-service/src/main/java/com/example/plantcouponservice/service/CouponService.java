package com.example.plantcouponservice.service;

import com.example.plantcouponservice.common.exception.CouponNotFoundException;
import com.example.plantcouponservice.common.producer.CouponCreateProducer;
import com.example.plantcouponservice.domain.Coupon;
import com.example.plantcouponservice.domain.CouponStatusEnum;
import com.example.plantcouponservice.repository.AppliedUserRepository;
import com.example.plantcouponservice.repository.CouponCountRepository;
import com.example.plantcouponservice.repository.CouponRepository;
import com.example.plantcouponservice.vo.CouponRequestDto;
import com.example.plantcouponservice.vo.CouponResponseDto;
import com.example.plantcouponservice.vo.StatusResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;
    private final CouponCreateProducer couponCreateProducer;
    private final AppliedUserRepository appliedUserRepository;

    /**
     * 쿠폰 발급
     * 요구사항 정리 : 하루에 중고 거래시 할인받을수 있는 100개 쿠폰을 발급
     * 쿠폰은 다음날 자정에 일괄 삭제
     * 100개 넘으면 발급 불가
     * @param : CouponRequestDto couponRequestDto
     */
    public StatusResponseDto applyCoupon(CouponRequestDto couponRequestDto) {
        // coupon 발급 전에 redis 싱글스레드 1증가
        Long add = appliedUserRepository.add(couponRequestDto.getMemberNo());
        if (add != 1) {
            return StatusResponseDto.addStatus(409);

        }
        Long count = couponCountRepository.increment();
        //오늘 날짜 기준으로 100개보다 많으면 return
        if (count > 100) {
            return StatusResponseDto.addStatus(429);
        }

        couponCreateProducer.create(couponRequestDto);
        return StatusResponseDto.success();
    }

    /**
     * 본인 쿠폰 조회
     * @param : Integer memberNo
     */
    @Transactional(readOnly = true)
    public List<CouponResponseDto> getCoupon(Integer memberNo) {
        LocalDateTime now = LocalDateTime.now(); // 현재 날짜 및 시간
        LocalDateTime thirtyOneDaysBefore = now.minusDays(31); // 31일 전

        List<Coupon> couponList = couponRepository.findByRegDateBetween(memberNo, thirtyOneDaysBefore, now, CouponStatusEnum.사용가능);
        if (couponList.isEmpty()) {
            log.info("쿠폰이 존재하지 않습니다");
            throw new CouponNotFoundException("쿠폰이 존재하지 않습니다");
        }
        return couponList.stream().map(coupon -> CouponResponseDto.builder()
                .couponNo(coupon.getCouponNo())
                .memberNo(memberNo)
                .discountPrice(coupon.getDiscountPrice())
                .regDate(coupon.getRegDate())
                .build())
                .collect(Collectors.toList());
    }

    /**
     * 쿠폰 사용 메서드
     * feignclient를 통해 결제 서비스의 결제 메서드와 하나의 작업단위로 이뤄야 됨
     * @param : Integer memberNo
     */
    @Transactional
    public CouponResponseDto useCoupon(Integer memberNo, Long couponNo) {
        Coupon coupon = couponRepository.findByMemberNoAndCouponNo(memberNo, couponNo);
        //사용완료
        coupon.useCoupon();
        CouponResponseDto couponResponseDto = CouponResponseDto.builder()
                .memberNo(memberNo)
                .discountPrice(3000)
                .build();
        couponRepository.save(coupon);
        return couponResponseDto;
    }
}
