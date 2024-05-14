package Plant.PlantProject.service.plantinfo;

import Plant.PlantProject.common.exception.ErrorCode;
import Plant.PlantProject.domain.Entity.Plant;
import Plant.PlantProject.repository.plantinfo.PlantRepository;
import Plant.PlantProject.vo.response.PlantResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PlantService {
    private final PlantRepository plantRepository;

    /**
     * 식물 전체 조회
     * @param : String search(식물 이름 포함), Pageable pageable
     */
    public Page<PlantResponseDto> plantList(String search, Pageable pageable) {
        Page<Plant> plants = plantRepository.findByPlantNameContaining(search, pageable);
        return plants.map(plant -> PlantResponseDto.convertPlantToDto(plant));
    }
    /**
     * 식물 정보 자세히 보기
     * @param : Long id(식물 pk)
     */
    public PlantResponseDto plantDetail(Long id) {
        Plant plant = plantRepository.findById(id).orElseThrow(ErrorCode::throwPlantNotFound);
        return PlantResponseDto.convertPlantToDto(plant);

    }
    /**
     * 식물 조회 동적 쿼리
     * 조건 1: 식물 카테고리 별(부분일치)
     * 조건 2 : 식물 관리 난이도 (완전일치)
     * @param : Map<String, String> searchCondition
     */
    public List<PlantResponseDto> getPlantByCondition(Map<String, String> searchCondition) {
        List<Plant> plantList = plantRepository.search(searchCondition);

        List<PlantResponseDto> plantResponseDtoList = plantList.stream().map(plant -> {
            PlantResponseDto plantResponseDto = PlantResponseDto.convertPlantToDto(plant);
            return plantResponseDto;
        }).collect(Collectors.toList());

        return plantResponseDtoList;


    }



}
