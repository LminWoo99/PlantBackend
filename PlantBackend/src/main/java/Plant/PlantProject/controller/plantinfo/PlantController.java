package Plant.PlantProject.controller.plantinfo;

import Plant.PlantProject.service.plantinfo.PlantService;
import Plant.PlantProject.vo.response.PlantResponseDto;
import Plant.PlantProject.service.plantinfo.PlantApi;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlantController {
    private final PlantService plantService;
    @GetMapping("plantList")
    @Operation(summary = "식구 도감- 식물 조회", description = "식물 전체 정보를 조회 할 수 있는 API")
    private ResponseEntity<Page<PlantResponseDto>> plantList(@RequestParam(required = false, defaultValue = "") String search
            , @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<PlantResponseDto> plantResponseDtoList = plantService.plantList(search, pageable);
        return ResponseEntity.ok().body(plantResponseDtoList);
    }

    @GetMapping("/plantList/{id}")
    @Operation(summary = "식구 도감- 식물 자세한 정보 조회", description = "식물에 관한 자세한 정보를 조회 할 수 있는 API")
    private ResponseEntity<PlantResponseDto> plantDetail(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(plantService.plantDetail(id));
    }
    @GetMapping("/plantList/condition")
    @Operation(summary = "식구 도감- 식물 조건 별 정보 조회", description = "식물에 관한 정보를 동적 쿼리를 활용하여 조건 별로 조회 할 수 있는 API")
    public ResponseEntity<List<PlantResponseDto>> getPlantByCondition(@RequestParam Map<String, String> searchCondition){
        List<PlantResponseDto> plantResponseDtoList = plantService.getPlantByCondition(searchCondition);

        return ResponseEntity.ok().body(plantResponseDtoList);
    }
}
