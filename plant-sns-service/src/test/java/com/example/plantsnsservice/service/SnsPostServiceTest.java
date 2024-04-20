package com.example.plantsnsservice.service;

import com.example.plantsnsservice.common.exception.CustomException;
import com.example.plantsnsservice.domain.entity.SnsPost;
import com.example.plantsnsservice.repository.querydsl.SnsPostRepository;
import com.example.plantsnsservice.vo.request.SnsPostRequestDto;
import com.example.plantsnsservice.vo.response.SnsPostResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SnsPostServiceTest {
    @Mock
    SnsPostRepository snsPostRepository;
    @Mock
    SnsHashTagMapService snsHashTagMapService;
    @InjectMocks
    SnsPostService snsPostService;
    @Test
    @DisplayName("생성 단위 테스트")
    void createPostTest() {
        //given
        SnsPostRequestDto snsPostRequestDto=SnsPostRequestDto.builder()
                .id(1L)
                .snsPostTitle("sns 게시글 테스트")
                .snsPostContent("테스트")
                .createdAt(LocalDateTime.now())
                .build();
        //when
//        snsPostService.createPost(snsPostRequestDto, any());

        verify(snsPostRepository, times(1)).save(any(SnsPost.class));


    }

    @Test
    @DisplayName("sns post 업데이트 테스트")
    void updateSnsPostTest() {
        //given
        SnsPost snsPost=SnsPost.builder()
                .snsPostTitle("sns 게시글 테스트")
                .snsPostContent("테스트")
                .build();
        SnsPostRequestDto snsPostRequestDto=SnsPostRequestDto.builder()
                .id(1L)
                .snsPostTitle("sns 게시글 테스트")
                .snsPostContent("테스트")
                .createdAt(LocalDateTime.now())
                .build();
        when(snsPostRepository.findById(1L)).thenReturn(Optional.ofNullable(snsPost));
        //when
        snsPostService.updateSnsPost(snsPostRequestDto);

        verify(snsPostRepository, times(1)).save(any(SnsPost.class));
    }
    @Test
    @DisplayName("존재하지 않는 sns post 업데이트 예외 테스트")
    void updateSnsPostExceptionTest() {
        //given

        SnsPostRequestDto snsPostRequestDto=SnsPostRequestDto.builder()
                .id(1L)
                .snsPostTitle("sns 게시글 테스트")
                .snsPostContent("테스트")
                .createdAt(LocalDateTime.now())
                .build();
        when(snsPostRepository.findById(1L)).thenReturn(Optional.empty());
        //when
        assertThrows(CustomException.class, () -> {
            snsPostService.updateSnsPost(snsPostRequestDto);
        }, "해당 게시글 정보를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("sns 게시글 단위 테스트")
    void getSnsPostListTest() {
        SnsPost snsPost=SnsPost.builder()
                .snsPostTitle("sns 게시글 테스트")
                .snsPostContent("테스트")
                .build();
        //given
        SnsPost snsPost2=SnsPost.builder()
                .snsPostTitle("sns 게시글 테스트2")
                .snsPostContent("테스트")
                .build();
        List<SnsPost> snsPostList = new ArrayList<>();
        snsPostList.add(snsPost);
        snsPostList.add(snsPost2);
        when(snsPostRepository.findAllByOrderByCreatedAtDesc()).thenReturn(snsPostList);
        when(snsHashTagMapService.findHashTagListBySnsPost(snsPost)).thenReturn(null);
        //when
        List<SnsPostResponseDto> expectedList = snsPostService.getSnsPostList();

        assertThat(expectedList.get(0).getSnsPostTitle()).isEqualTo("sns 게시글 테스트");
        assertThat(expectedList.size()).isEqualTo(2);
    }
    @Test
    @DisplayName("해시태그 기준으로 sns 게시글 조회 단위 테스트")
    void findAllByHashTagTest() throws Exception{
        //given
        String hashTagName = "#LeeMinWoo";
        List hashTagNameList = new ArrayList<>();

        hashTagNameList.add(hashTagName);

        SnsPostResponseDto snsPostRequestDto=SnsPostResponseDto.builder()
                .id(1L)
                .snsPostTitle("sns 게시글 테스트")
                .snsPostContent("테스트")
                .hashTags(hashTagNameList)
                .build();
        SnsPostResponseDto snsPostRequestDto1=SnsPostResponseDto.builder()
                .id(2L)
                .snsPostTitle("sns 게시글 테스트2")
                .snsPostContent("테스트")
                .hashTags(hashTagNameList)
                .build();
        List<SnsPostResponseDto> expectedList = new ArrayList<>();
        expectedList.add(snsPostRequestDto);
        expectedList.add(snsPostRequestDto1);

        when(snsPostRepository.findAllByHashTag(hashTagName)).thenReturn(expectedList);
        //when
        List<SnsPostResponseDto> resultList = snsPostService.findAllByHashTag(hashTagName);

        //then
        assertThat(resultList.size()).isEqualTo(2);
        assertThat(resultList.get(0).getHashTags().get(0)).isEqualTo("#LeeMinWoo");
    }
}