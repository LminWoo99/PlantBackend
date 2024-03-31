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
    @Column(name = "trade_board_no")
    private Long tradeBoardNo;
    @Column(name = "discount_price")
    private Integer discountPrice;
    private LocalDateTime regDate;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "type")
    private CouponStatusEnum type;

    @Builder
    public Coupon(Integer memberNo, Long tradeBoardNo, Integer discountPrice, LocalDateTime regDate) {
        this.memberNo = memberNo;
        this.tradeBoardNo = tradeBoardNo;
        this.discountPrice = discountPrice;
        this.regDate = regDate;
        this.type = CouponStatusEnum.사용가능;
    }

    public void useCoupon() {
        this.type = CouponStatusEnum.사용완료;
    }
}
