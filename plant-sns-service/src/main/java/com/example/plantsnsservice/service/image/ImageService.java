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
    private final SnsPostService snsPostService;
    private final AwsS3Service awsS3Service;
    @Transactional
    public void uploadImagesToSnsPost(Long snsPostId, List<MultipartFile> files) throws IOException {
        log.info("Uploading images for SnsPostId ID: {}", snsPostId);
        SnsPostResponseDto snsPostResponseDto = snsPostService.findById(snsPostId);
        saveImages(files, snsPostResponseDto);
    }

    private List<Image> saveImages(List<MultipartFile> files, SnsPostResponseDto snsPostResponseDto) throws IOException {
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            images.add(uploadImage(file, snsPostResponseDto));
        }
        imageRepository.saveAll(images);
        return images;
    }

    private Image uploadImage(MultipartFile file, SnsPostResponseDto snsPostResponseDto) throws IOException {
        log.info("Uploading image to storage server for file: {}", file.getOriginalFilename());
        String filename = FileUtils.getRandomFilename();
        String filepath = awsS3Service.upload(file, filename);

        SnsPost snsPost = SnsPost.builder()
                .snsPostTitle(snsPostResponseDto.getSnsPostTitle())
                .snsPostContent(snsPostResponseDto.getSnsPostContent())
                .memberNo(snsPostResponseDto.getMemberNo())
                .build();

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
