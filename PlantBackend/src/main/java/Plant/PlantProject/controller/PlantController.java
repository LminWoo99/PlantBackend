package Plant.PlantProject.controller;

import Plant.PlantProject.dto.PlantDto;
import Plant.PlantProject.service.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlantController {
    private final PlantService plantService;
    @GetMapping("plantList")
    private ResponseEntity<Page<PlantDto>> plantList(@RequestParam(required = false, defaultValue = "") String search, @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC)
    Pageable pageable) {
        Page<PlantDto> plantDtos = plantService.plantList(search, pageable);
        return ResponseEntity.ok(plantDtos);
    }

    @GetMapping("/plantList/{id}")
    private ResponseEntity<PlantDto> plantDetail(@PathVariable("id") Long id) {
        System.out.println("plant 호출");
        return ResponseEntity.ok().body(plantService.plantDetail(id));
    }
    @GetMapping("/plant")
    private void start() throws IOException {
        plantService.start();
    }
}
