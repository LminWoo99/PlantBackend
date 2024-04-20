package com.example.plantsnsservice.service.image;

import com.example.plantsnsservice.domain.FileUtils;
import com.example.plantsnsservice.domain.entity.Image;
import com.example.plantsnsservice.domain.entity.SnsPost;
import com.example.plantsnsservice.repository.ImageRepository;
import com.example.plantsnsservice.service.SnsPostService;
import com.example.plantsnsservice.vo.response.SnsPostResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    private final ImageRepository imageRepository;
    private final AwsS3Service awsS3Service;
    @Transactional
    public void uploadImagesToSnsPost(SnsPost snsPost, List<MultipartFile> files) throws IOException {
        log.info("Uploading images for SnsPostId ID: {}", snsPost.getId());
        saveImages(files, snsPost);
    }
    @Transactional
    public List<Image> saveImages(List<MultipartFile> files, SnsPost snsPost) throws IOException {
        List<Image> images = new ArrayList<>();


        for (MultipartFile file : files) {
            Image image = uploadImage(file, snsPost);
            //양방향 연관관계 메서드 사용
            snsPost.addImageList(image);
            images.add(image);
        }
        imageRepository.saveAll(images);
        return images;
    }

    private Image uploadImage(MultipartFile file, SnsPost snsPost) throws IOException {
        log.info("Uploading image to storage server for file: {}", file.getOriginalFilename());
        String filename = FileUtils.getRandomFilename();
        String filepath = awsS3Service.upload(file, filename);


        return Image.builder()
                .name(filename)
                .url(filepath)
                .snsPost(snsPost)
                .build();
    }
    @Transactional(readOnly = true)
    public List<Image> findImagesBySnsPost(Long snsPostId) {
        return imageRepository.findImageBySnsPostId(snsPostId);
    }
}
