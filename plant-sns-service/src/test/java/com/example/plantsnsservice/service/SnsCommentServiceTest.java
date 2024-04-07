package com.example.plantsnsservice.service;

import com.example.plantsnsservice.common.exception.CustomException;
import com.example.plantsnsservice.domain.SnsComment;
import com.example.plantsnsservice.domain.SnsPost;
import com.example.plantsnsservice.repository.SnsPostRepository;
import com.example.plantsnsservice.repository.querydsl.SnsCommentRepository;
import com.example.plantsnsservice.vo.request.SnsCommentRequestDto;
import com.example.plantsnsservice.vo.request.SnsPostRequestDto;
import com.example.plantsnsservice.vo.response.SnsCommentResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class SnsCommentServiceTest {
    @Mock
    SnsCommentRepository snsCommentRepository;
    @Mock
    SnsPostRepository snsPostRepository;
    @InjectMocks
    SnsCommentService snsCommentService;

    @Test
    @DisplayName("댓글 작성 단위 테스트")
    void createCommentTest() {
        //given
        SnsCommentRequestDto snsCommentRequestDto = SnsCommentRequestDto.builder()
                .snsPostId(1L)
                .content("댓글 작성 테스트")
                .createdBy("Lee")
                .build();
        SnsCommentResponseDto snsCommentResponseDto = SnsCommentResponseDto.builder()
                .id(1L)
                .content("댓글 작성 테스트")
                .createdBy("Lee")
                .build();
        SnsPost snsPost= SnsPost.builder()
                .snsPostTitle("test")
                .snsPostContent("댓글 작성 테스트")
                .memberNo(1L)
                .build();

        when(snsPostRepository.findById(1L)).thenReturn(Optional.ofNullable(snsPost));
        //when
        SnsCommentResponseDto expectedDto = snsCommentService.createComment(snsCommentRequestDto);
        //then
        Assertions.assertThat(expectedDto.getCreatedBy()).isEqualTo("Lee");
        verify(snsCommentRepository, times(1)).save(any());

    }
    @Test
    @DisplayName("댓글 작성 예외 테스트 - 게시글이 없을 경우")
    void createCommentExceptionTest() {
        //given
        SnsCommentRequestDto snsCommentRequestDto = SnsCommentRequestDto.builder()
                .snsPostId(1L)
                .content("댓글 작성 테스트")
                .createdBy("Lee")
                .build();


        when(snsPostRepository.findById(1L)).thenReturn(Optional.empty());
        //then
        assertThrows(CustomException.class, () -> {
            snsCommentService.createComment(snsCommentRequestDto);
        }, "해당 게시글 정보를 찾을 수 없습니다.");

    }

    @Test
    @DisplayName("댓글 조회 단위 테스트 - 중첩 구조 변환 확인")
    void findCommentListByPostIdTest() {
        SnsPost snsPost= SnsPost.builder()
                .snsPostTitle("test")
                .snsPostContent("댓글 작성 테스트")
                .memberNo(1L)
                .build();
        SnsComment parentComment= SnsComment.builder()
                .snsPost(snsPost)
                .content("댓글 작성 테스트")
                .createdBy("Lee")
                .parent(null)
                .build();
        SnsComment childComment= SnsComment.builder()
                .snsPost(snsPost)
                .content("댓글 작성 테스트2")
                .createdBy("Lee")
                .parent(parentComment)
                .build();
        List<SnsComment> snsCommentList = new ArrayList<>();

        snsCommentList.add(parentComment);
        snsCommentList.add(childComment);

        when(snsCommentRepository.findSnsCommentByPostId(snsPost.getId()))
                .thenReturn(snsCommentList);

        List<SnsCommentResponseDto> result = snsCommentService.findCommentListByPostId(snsPost.getId());

        //대댓글은 댓글 중첨구조로 바뀌면서 list size가 1이어야함
        Assertions.assertThat(result.size()).isEqualTo(1);
        verify(snsCommentRepository).findSnsCommentByPostId(snsPost.getId());
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void deleteSnsCommentTest() {
        //given
        Long snsCommentId = 1L;
        //when
        snsCommentService.deleteSnsComment(snsCommentId);
        //then
        verify(snsCommentRepository, times(1)).deleteById(1L);
    }
}