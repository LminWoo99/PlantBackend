package Plant.PlantProject.repository;

import Plant.PlantProject.Entity.Plant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {
    Page<Plant> findByPlantNameContaining(String plantName, Pageable pageable);

    Plant findByContentNum(String contentNum);
}