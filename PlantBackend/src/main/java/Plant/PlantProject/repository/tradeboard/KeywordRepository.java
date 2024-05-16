package Plant.PlantProject.repository.tradeboard;

import Plant.PlantProject.domain.Entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    @Query("SELECT k.memberNo FROM Keyword k WHERE k.keywordContent = :keywordContent")
    List<Integer> findMemberNosByKeywordContent(String keywordContent);

    List<Keyword> findByMemberNo(Integer memberNo);
}
