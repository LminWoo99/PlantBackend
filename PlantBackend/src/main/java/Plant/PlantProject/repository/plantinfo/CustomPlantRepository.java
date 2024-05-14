package Plant.PlantProject.repository.plantinfo;

import Plant.PlantProject.domain.Entity.Plant;
import Plant.PlantProject.vo.response.PlantResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CustomPlantRepository {
    List<Plant> search(final Map<String, String> searchCondition);

}
