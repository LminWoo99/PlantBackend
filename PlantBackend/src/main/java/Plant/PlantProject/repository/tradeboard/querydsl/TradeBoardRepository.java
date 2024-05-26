package Plant.PlantProject.repository.tradeboard.querydsl;

import Plant.PlantProject.domain.Entity.TradeBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
 작성자 : 이민우
 작성 일자: 02.19
 내용 : 거래 게시글 저장소  구현(스프링 데이터 상속 받음)
 특이 사항: 없음
*/
public interface TradeBoardRepository extends JpaRepository<TradeBoard, Long>, CustomTradeBoardRepository {
    List<TradeBoard> findTradeBoardByMemberId(Long memberId);
    List<TradeBoard> findTradeBoardByBuyer(String buyer);
}
