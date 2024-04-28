package Plant.PlantProject.controller;

import Plant.PlantProject.domain.Entity.Image;
import Plant.PlantProject.service.tradeboard.ImageFileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    private final ImageFileUploadService imageFileUploadService;

    @PostMapping("/{tradeBoardId}/images")
    @Operation(summary = "이미지 업로드", description = "Amazon S3를 이용해 이미지를 업로드 할 수 있는 API")
    private ResponseEntity<List<Image>> uploadImages(@PathVariable Long tradeBoardId,
                                               @RequestParam("file")List<MultipartFile> files) throws IOException{
        log.info("image 호출");
        List<Image> upload = imageFileUploadService.uploadImagesToTradeBoard(tradeBoardId, files);
        return ResponseEntity.ok().body(upload);
    }
    @GetMapping("/{tradeBoardId}/images")
    @Operation(summary = "이미지 조회", description = "이미지 조회 할 수 있는 API")
    private ResponseEntity<List<Image>> images(@PathVariable Long tradeBoardId){
        return ResponseEntity.ok().body(imageFileUploadService.findImagesByTradeBoardId(tradeBoardId));

    }

}
