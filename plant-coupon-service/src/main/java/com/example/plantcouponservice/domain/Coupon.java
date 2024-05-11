package com.example.plantcouponservice.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_no")
    private Long couponNo;
    @Column(name = "member_no")
    private Integer memberNo;
    @Column(name = "discount_price")
    private Integer discountPrice;
    private LocalDateTime regDate;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "type")
    private CouponStatusEnum type;

    @Builder
    public Coupon(Integer memberNo, Integer discountPrice, LocalDateTime regDate) {
        this.memberNo = memberNo;
        this.discountPrice = discountPrice;
        this.regDate = regDate;
        this.type = CouponStatusEnum.사용가능;
    }

    public void useCoupon() {
        this.type = CouponStatusEnum.사용완료;
    }
    public void revertCoupon() {
        this.type = CouponStatusEnum.사용가능;
    }
}
