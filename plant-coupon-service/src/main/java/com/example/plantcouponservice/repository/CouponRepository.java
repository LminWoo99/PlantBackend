package com.example.plantcouponservice.repository;

import com.example.plantcouponservice.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Query("SELECT COUNT(c) FROM Coupon c WHERE c.regDate >= :startOfDay AND c.regDate <= :endOfDay")
    Long countByRegDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    Coupon findByMemberNo(Integer memberNo);
}
