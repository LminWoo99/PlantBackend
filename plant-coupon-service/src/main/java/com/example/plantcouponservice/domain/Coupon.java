package com.example.plantcouponservice.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

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
    @Builder
    public Coupon(Integer memberNo, Long tradeBoardNo, Integer discountPrice) {
        this.memberNo = memberNo;
        this.tradeBoardNo = tradeBoardNo;
        this.discountPrice = discountPrice;
    }
}
