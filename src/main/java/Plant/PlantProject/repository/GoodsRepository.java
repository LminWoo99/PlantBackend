package Plant.PlantProject.repository;

import Plant.PlantProject.Entity.Goods;
import Plant.PlantProject.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {
    List<Goods> findByMemberId(Long memberId);
    List<Goods> findByTradeBoardId(Long tradeBoardId);

    Optional<Goods> findByMemberIdAndTradeBoardId(Long memberId, Long tradeBoardId);

}
