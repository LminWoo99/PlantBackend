package Plant.PlantProject.domain.Entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

/*
 작성자 : 이민우
 작성 일자: 02.18
 내용 : 찜 목록 엔티티 코드
 특이 사항: 없음
*/
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tradeBoard_id")
    private TradeBoard tradeBoard;

    private boolean goodsStatus;


    public void setGoodsStatus(boolean goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public static Goods createGoods(Member member, TradeBoard tradeBoard){
        Goods goods = new Goods();
        goods.member = member;
        goods.tradeBoard = tradeBoard;
        goods.goodsStatus=true;
        return goods;
    }



}
