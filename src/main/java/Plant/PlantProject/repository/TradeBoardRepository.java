package Plant.PlantProject.repository;

import Plant.PlantProject.Entity.TradeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
/*
 작성자 : 이민우
 작성 일자: 02.19
 내용 : 거래 게시글 저장소  구현(스프링 데이터 상속 받음)
 특이 사항: 없음
*/
public interface TradeBoardRepository extends JpaRepository<TradeBoard, Long> {


}
