package com.example.plantsnsservice.service;

import com.example.plantsnsservice.domain.entity.SnsPost;
import com.example.plantsnsservice.repository.querydsl.SnsPostRepository;
import com.example.plantsnsservice.vo.request.SnsPostRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SnsPostServiceLikesMethodTest {
    @Autowired
    SnsPostRepository snsPostRepository;
    @Autowired
    SnsPostService snsPostService;
    @Autowired
    SnsPostServiceFacade snsPostServiceFacade;
    @Test
    void updateSnsLikesCount() throws InterruptedException, IOException {
        //given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        Set<String> hashTags = new HashSet<>();
        List<MultipartFile> files = new ArrayList<>();
        hashTags.add("나무");
        SnsPostRequestDto snsPostRequestDto=SnsPostRequestDto.builder()
                .id(1L)
                .snsPostTitle("sns 게시글 테스트")
                .snsPostContent("테스트")
                .snsLikesCount(1)
                .snsViewsCount(1)
                .hashTags(hashTags)
                .build();

        snsPostService.createPost(snsPostRequestDto, files);
        //when
        for (int i = 0; i<threadCount; i++) {

            executorService.submit(() -> {
                try{
                    snsPostServiceFacade.updateSnsLikesCountLock(1L,1);

                }
                finally {
                    countDownLatch.countDown();

                }
            });
        }
        countDownLatch.await();
        Thread.sleep(10000);
        Optional<SnsPost> byId = snsPostRepository.findById(1L);
        assertThat(byId.get().getSnsLikesCount()).isEqualTo(101);
    }
}