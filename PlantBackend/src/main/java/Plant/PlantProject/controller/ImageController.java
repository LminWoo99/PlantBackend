package Plant.PlantProject.controller;

import Plant.PlantProject.Entity.Image;
import Plant.PlantProject.service.ImageFileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private ResponseEntity<List<Image>> uploadImages(@PathVariable Long tradeBoardId,
                                               @RequestParam("file")List<MultipartFile> files) throws IOException{
        log.info("image 호출");
        List<Image> upload = imageFileUploadService.upload(tradeBoardId, files);
        return ResponseEntity.ok().body(upload);
    }
    @GetMapping("/{tradeBoardId}/images")
    private ResponseEntity<List<Image>> images(@PathVariable Long tradeBoardId){
        return ResponseEntity.ok().body(imageFileUploadService.findImageByTradeBoardId(tradeBoardId));

    }

}
