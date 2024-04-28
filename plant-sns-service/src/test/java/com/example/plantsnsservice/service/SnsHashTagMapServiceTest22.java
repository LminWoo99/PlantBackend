//package com.example.plantsnsservice.service;
//
//import com.example.plantsnsservice.domain.entity.HashTag;
//import com.example.plantsnsservice.domain.entity.SnsHashTagMap;
//import com.example.plantsnsservice.domain.entity.SnsPost;
//import com.example.plantsnsservice.repository.SnsHashTagMapRepository;
//import com.example.plantsnsservice.repository.querydsl.SnsPostRepository;
//import com.example.plantsnsservice.vo.request.SnsPostRequestDto;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.*;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//class SnsHashTagMapServiceTest22 {
//    @Autowired
//    SnsPostService snsPostService;
//    @Autowired
//    SnsPostRepository snsPostRepository;
//    @Autowired
//    SnsHashTagMapRepository snsHashTagMapRepository;
//    @BeforeEach
//    void setUp() throws IOException {
//        Set<String> hashTags = new HashSet<>();
//        hashTags.add("test");
//        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
//        List<MultipartFile> files = Arrays.asList(file);
//        SnsPostRequestDto snsPostRequestDto= SnsPostRequestDto.builder()
//                .snsPostTitle("test title")
//                .snsPostContent("test content")
//                .createdBy("lee")
//                .hashTags(hashTags)
//                .snsViewsCount(0)
//                .snsLikesCount(0)
//                .build();
//        snsPostService.createPost(snsPostRequestDto, files);
//    }
//
//    @Test
//    @Transactional
//    void findHashTagListBySnsPost() {
//        Optional<SnsPost> byId = snsPostRepository.findById(1L);
//        SnsPost snsPost = byId.get();
//        assertThat(snsPost.getSnsLikesCount()).isEqualTo(1);
//    }
//}