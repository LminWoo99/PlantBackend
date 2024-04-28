package com.example.plantsnsservice.service;

import com.example.plantsnsservice.common.exception.CustomException;
import com.example.plantsnsservice.domain.entity.SnsPost;
import com.example.plantsnsservice.repository.querydsl.SnsPostRepository;
import com.example.plantsnsservice.repository.redis.SnsLikesCountRepository;
import com.example.plantsnsservice.service.image.ImageService;
import com.example.plantsnsservice.vo.request.SnsPostRequestDto;
import com.example.plantsnsservice.vo.response.SnsCommentResponseDto;
import com.example.plantsnsservice.vo.response.SnsPostResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class SnsPostServiceTest {
    @Mock
    SnsPostRepository snsPostRepository;
    @Mock
    SnsHashTagMapService snsHashTagMapService;
    @Mock
    ImageService imageService;
    @Mock
    SnsCommentService snsCommentService;
    @Mock
    SnsLikesCountRepository snsLikesCountRepository;

    @InjectMocks
    SnsPostService snsPostService;
    @Test
    @DisplayName("생성 단위 테스트")
    void createPostTest() throws IOException {
        //given
        SnsPostRequestDto snsPostRequestDto=SnsPostRequestDto.builder()
                .id(1L)
                .snsPostTitle("sns 게시글 테스트")
                .snsPostContent("테스트")
                .createdAt(LocalDateTime.now())
                .build();

        SnsPost snsPost = SnsPost.builder()
                .snsPostTitle(snsPostRequestDto.getSnsPostTitle())
                .snsPostContent(snsPostRequestDto.getSnsPostContent())
                .build();
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        List<MultipartFile> files = Arrays.asList(file);

        when(snsPostRepository.save(any(SnsPost.class))).thenReturn(snsPost);

        //when
        snsPostService.createPost(snsPostRequestDto, files);

        verify(snsPostRepository, times(1)).save(any(SnsPost.class));

    }

    @Test
    @DisplayName("sns post 업데이트 테스트")
    void updateSnsPostTest() {
        //given
        String hashTagName = "#LeeMinWoo";
        Set hashTagNameList = new HashSet<>();

        hashTagNameList.add(hashTagName);

        SnsPost snsPost=SnsPost.builder()
                .snsPostTitle("sns 게시글 테스트")
                .snsPostContent("테스트")
                .build();
        SnsPostRequestDto snsPostRequestDto=SnsPostRequestDto.builder()
                .id(1L)
                .snsPostTitle("sns 게시글 테스트")
                .snsPostContent("테스트")
                .createdAt(LocalDateTime.now())
                .hashTags(hashTagNameList)
                .build();
        when(snsPostRepository.findById(1L)).thenReturn(Optional.ofNullable(snsPost));
        //when
        snsPostService.updateSnsPost(snsPostRequestDto);

        verify(snsHashTagMapService, times(1)).deleteSnsHashTagMap(snsPost.getId());
        verify(snsHashTagMapService, times(1)).createHashTag(snsPost , hashTagNameList);
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
    @DisplayName("sns 게시글  전체 조회 단위 테스트")
    void getSnsPostListTest() {
        //given
        SnsPost snsPost=SnsPost.builder()
                .snsPostTitle("sns 게시글 테스트")
                .snsPostContent("테스트")
                .build();

        SnsPost snsPost2=SnsPost.builder()
                .snsPostTitle("sns 게시글 테스트2")
                .snsPostContent("테스트")
                .build();
        List<SnsPost> snsPostList = new ArrayList<>();
        snsPostList.add(snsPost);
        snsPostList.add(snsPost2);

        List<SnsCommentResponseDto> snsCommentResponseDtoList = new ArrayList<>();
        SnsCommentResponseDto snsCommentResponseDto = SnsCommentResponseDto.builder()
                .id(1L)
                .content("댓글 작성 테스트")
                .createdBy("Lee")
                .build();
        snsCommentResponseDtoList.add(snsCommentResponseDto);

        when(snsPostRepository.findAllByOrderByCreatedAtDesc()).thenReturn(snsPostList);
        when(snsHashTagMapService.findHashTagListBySnsPost(snsPost)).thenReturn(null);
        when(snsCommentService.findCommentListByPostId(snsPost.getId())).thenReturn(snsCommentResponseDtoList);
        //when
        List<SnsPostResponseDto> expectedList = snsPostService.getSnsPostList();

        assertThat(expectedList.get(0).getSnsPostTitle()).isEqualTo("sns 게시글 테스트");
        assertThat(expectedList.size()).isEqualTo(2);
        verify(snsCommentService, times(2)).findCommentListByPostId(snsPost.getId());
    }

    @Test
    @DisplayName("닉네임으로 sns 게시글 조회 단위 테스트")
    void getSnsPostByCreatedTest() throws Exception{
        //given
        SnsPost snsPost=SnsPost.builder()
                .snsPostTitle("sns 게시글 테스트")
                .snsPostContent("테스트")
                .createdBy("민우")
                .build();
        SnsPost snsPost1=SnsPost.builder()
                .snsPostTitle("sns 게시글 테스트")
                .snsPostContent("테스트")
                .createdBy("민우")
                .build();
        List<SnsPost> requestList = new ArrayList<>();

        requestList.add(snsPost);
        requestList.add(snsPost1);

        SnsPostResponseDto snsPostRequestDto=SnsPostResponseDto.builder()
                .id(1L)
                .snsPostTitle("sns 게시글 테스트")
                .snsPostContent("테스트")
                .createdBy("민우")
                .build();
        SnsPostResponseDto snsPostRequestDto1=SnsPostResponseDto.builder()
                .id(2L)
                .snsPostTitle("sns 게시글 테스트")
                .snsPostContent("테스트")
                .createdBy("민우")
                .build();
        List<SnsPostResponseDto> expectedReturnList = new ArrayList<>();

        expectedReturnList.add(snsPostRequestDto);
        expectedReturnList.add(snsPostRequestDto1);

        when(snsPostRepository.findAllByCreatedBy("민우")).thenReturn(requestList);

        //when
        List<SnsPostResponseDto> snsPostByCreated = snsPostService.getSnsPostByCreated("민우");
        //then
        assertThat(snsPostByCreated.size()).isEqualTo(expectedReturnList.size());
        assertThat(snsPostByCreated.get(0).getCreatedBy()).isEqualTo("민우");
    }
}