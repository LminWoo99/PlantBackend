package Plant.PlantProject.repository.plantinfo;

import Plant.PlantProject.domain.Entity.Plant;

import java.util.List;
import java.util.Map;

public interface CustomPlantRepository {
    List<Plant> search(final Map<String, String> searchCondition);

}
