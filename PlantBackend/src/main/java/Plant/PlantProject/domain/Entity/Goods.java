package Plant.PlantProject.domain.Entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

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


    public static Goods createGoods(Member member, TradeBoard tradeBoard){
        Goods goods = new Goods();
        goods.member = member;
        goods.tradeBoard = tradeBoard;
        goods.goodsStatus=true;
        return goods;
    }

}
