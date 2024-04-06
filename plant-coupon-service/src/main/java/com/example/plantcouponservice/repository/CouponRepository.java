package com.example.plantcouponservice.repository;

import com.example.plantcouponservice.domain.Coupon;
import com.example.plantcouponservice.domain.CouponStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Query("SELECT COUNT(c) FROM Coupon c WHERE c.regDate >= :startOfDay AND c.regDate <= :endOfDay")
    Long countByRegDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    @Query("SELECT c FROM Coupon c WHERE c.memberNo=:memberNo AND c.regDate >= :startOfDay AND c.regDate <= :endOfDay AND c.type=:couponStatusEnum ")
    List<Coupon> findByRegDateBetween(Integer memberNo, LocalDateTime startOfDay, LocalDateTime endOfDay, CouponStatusEnum couponStatusEnum);

    Coupon findByMemberNoAndCouponNo(Integer memberNo, Long couponNo);

    //clearAutomatically = true 자동으로 영속성 컨텍스트를 초기화
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM Coupon C WHERE C.type=:couponStatusEnum")
    void deleteByType(CouponStatusEnum couponStatusEnum);

}
