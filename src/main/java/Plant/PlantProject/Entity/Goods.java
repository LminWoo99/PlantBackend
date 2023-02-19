package Plant.PlantProject.Entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;

import java.time.LocalDateTime;

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
    @GeneratedValue
    @Column(name = "goodsId")
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tPost_id")
    private TradeBoard tradeBoard;
    @CreatedDate
    private LocalDateTime likeDate;



}
