package Plant.PlantProject.repository;

import Plant.PlantProject.Entity.TradeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/*
 작성자 : 이민우
 작성 일자: 02.19
 내용 : 거래 게시글 저장소  구현(스프링 데이터 상속 받음)
 특이 사항: 없음
*/
public interface TradeBoardRepository extends JpaRepository<TradeBoard, Long> {
    Page<TradeBoard> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

    List<TradeBoard> findTradeBoardByMemberId(Long memberId);
    @Modifying
    @Query("update TradeBoard t set t.view = t.view + 1 where t.id = :id")
    int updateView(@Param("id") Long id);


    TradeBoard findTradeBoardById(Long id);

    @Modifying
    @Query("UPDATE TradeBoard tb SET tb.goodCount = :goodCount WHERE tb.id = :id")
    void saveGoodsCount(@Param("id") Long id, @Param("goodCount") int goodCount);
}
