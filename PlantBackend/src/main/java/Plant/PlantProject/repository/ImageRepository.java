package Plant.PlantProject.repository;

import Plant.PlantProject.domain.Entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Image i SET i.removed = true WHERE i.tradeBoard.id = :tradeBoardId")
    public void saveAll(Long tradeBoardId);

    public List<Image> findImageByTradeBoardId(Long tradeBoardId);
}
