//package com.example.plantsnsservice.controller;
//
//import com.example.plantsnsservice.domain.entity.Image;
//import com.example.plantsnsservice.service.image.ImageService;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//public class ImageController {
//    private final ImageService imageService;
//
//    @PostMapping("/{snsPostId}/images")
//    @Operation(summary = "이미지 업로드", description = "Amazon S3를 이용해 이미지를 업로드 할 수 있는 API")
//    private ResponseEntity<HttpStatus> uploadImages(@PathVariable Long snsPostId,
//                                                     @RequestParam("file")List<MultipartFile> files) throws IOException {
//        imageService.uploadImagesToSnsPost(snsPostId, files);
//        return ResponseEntity.ok().body(HttpStatus.ACCEPTED);
//    }
//    @GetMapping("/{snsPostId}/images")
//    @Operation(summary = "이미지 조회", description = "이미지 조회 할 수 있는 API")
//    private ResponseEntity<List<Image>> images(@PathVariable Long snsPostId){
//        return ResponseEntity.ok().body(imageService.findImagesBySnsPost(snsPostId));
//
//    }
//}
