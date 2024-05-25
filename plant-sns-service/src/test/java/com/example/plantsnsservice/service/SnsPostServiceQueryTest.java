package com.example.plantsnsservice.service;

import com.example.plantsnsservice.vo.response.SnsPostResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class SnsPostServiceQueryTest {
    @Autowired
    SnsPostService snsPostService;
    @Test
    void getSnsPostList() {
        long startTime = System.nanoTime(); // 시작 시간 측정
        List<SnsPostResponseDto> snsPostList = snsPostService.getSnsPostList();
        long endTime = System.nanoTime(); // 종료 시간 측정

        long duration = (endTime - startTime) / 1_000_000; // 밀리초로 변환

        System.out.println("Query execution time: " + duration + " ms");

        System.out.println("snsPostList = " + snsPostList.size());
        assertThat(snsPostList).isNotNull();
        assertThat(snsPostList.size()).isGreaterThan(0);
    }
}