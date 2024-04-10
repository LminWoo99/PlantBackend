package com.example.plantsnsservice.service;

import com.example.plantsnsservice.domain.entity.HashTag;
import com.example.plantsnsservice.domain.entity.SnsHashTagMap;
import com.example.plantsnsservice.domain.entity.SnsPost;
import com.example.plantsnsservice.repository.SnsHashTagMapRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SnsHashTagMapServiceTest {
    @Mock
    SnsHashTagMapRepository snsHashTagMapRepository;
    @Mock
    HashTagService hashTagService;
    @InjectMocks
    SnsHashTagMapService snsHashTagMapService;

    @Test
    @DisplayName("해시 태그 생성 단위 테스트")
    void createHashTagTest() {
        //given
        SnsPost snsPost=SnsPost.builder()
                .snsPostTitle("sns 게시글 테스트")
                .snsPostContent("테스트")
                .memberNo(1L)
                .build();
        HashTag hashTag= HashTag.builder()
                .name("test")
                .build();
        Set<String> hashTags = new HashSet<>();
        hashTags.add("#test");


        when(hashTagService.findByName("#test")).thenReturn(Optional.empty());
        when(snsHashTagMapRepository.save(any())).thenReturn(new SnsHashTagMap(snsPost, hashTag));
        //when
        snsHashTagMapService.createHashTag(snsPost, hashTags);
        //then
        verify(hashTagService, times(1)).save(any());
    }
    @Test
    @DisplayName("해시 태그 생성시 중복 방지 단위 테스트")
    void createHashTagDuplicationTest() {
        //given
        SnsPost snsPost=SnsPost.builder()
                .snsPostTitle("sns 게시글 테스트")
                .snsPostContent("테스트")
                .memberNo(1L)
                .build();
        HashTag hashTag= HashTag.builder()
                .name("test")
                .build();
        Set<String> hashTags = new HashSet<>();
        hashTags.add("#test");
        hashTags.add("#test");


        when(hashTagService.findByName("#test")).thenReturn(Optional.empty());
        when(snsHashTagMapRepository.save(any())).thenReturn(new SnsHashTagMap(snsPost, hashTag));
        //when
        snsHashTagMapService.createHashTag(snsPost, hashTags);
        //then
        //1번만 저장되야함
        verify(hashTagService, times(1)).save(any());
    }
    @Test
    @DisplayName("sns 게시글에 저장된 해시태그 조회")
    void findHashTagListBySnsPostTest() {
        //given
        SnsPost snsPost=SnsPost.builder()
                .snsPostTitle("sns 게시글 테스트")
                .snsPostContent("테스트")
                .memberNo(1L)
                .build();

        HashTag hashTag= HashTag.builder()
                .name("#test")
                .build();
        HashTag hashTag2= HashTag.builder()
                .name("#test2")
                .build();

        SnsHashTagMap snsHashTagMap = new SnsHashTagMap(snsPost, hashTag);
        SnsHashTagMap snsHashTagMap2 = new SnsHashTagMap(snsPost, hashTag2);
        List<SnsHashTagMap> snsHashTagMapList = new ArrayList<>();

        snsHashTagMapList.add(snsHashTagMap);
        snsHashTagMapList.add(snsHashTagMap2);

        when(snsHashTagMapRepository.findAllBySnsPost(snsPost)).thenReturn(snsHashTagMapList);
        //when
        List<String> hashTagListBySnsPost = snsHashTagMapService.findHashTagListBySnsPost(snsPost);
        //then
        assertThat(hashTagListBySnsPost.size()).isEqualTo(2);
        assertThat(hashTagListBySnsPost.get(0)).isEqualTo("#test");
        assertThat(hashTagListBySnsPost.get(1)).isEqualTo("#test2");

    }
}