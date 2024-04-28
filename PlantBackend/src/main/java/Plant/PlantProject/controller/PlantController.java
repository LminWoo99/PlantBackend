package Plant.PlantProject.controller;

import Plant.PlantProject.domain.vo.response.PlantDto;
import Plant.PlantProject.service.plantinfo.PlantService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlantController {
    private final PlantService plantService;
    @GetMapping("plantList")
    @Operation(summary = "식구 도감- 식물 조회", description = "식물 전체 정보를 조회 할 수 있는 API")
    private ResponseEntity<Page<PlantDto>> plantList(@RequestParam(required = false, defaultValue = "") String search, @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC)
    Pageable pageable) {
        Page<PlantDto> plantDtos = plantService.plantList(search, pageable);
        return ResponseEntity.ok(plantDtos);
    }

    @GetMapping("/plantList/{id}")
    @Operation(summary = "식구 도감- 식물 자세한 정보 조회", description = "식물에 관한 자세한 정보를 조회 할 수 있는 API")
    private ResponseEntity<PlantDto> plantDetail(@PathVariable("id") Long id) {
        System.out.println("plant 호출");
        return ResponseEntity.ok().body(plantService.plantDetail(id));
    }
//    @GetMapping("/plant")
//    private void start() throws IOException {
//        plantService.start();
//    }
}
