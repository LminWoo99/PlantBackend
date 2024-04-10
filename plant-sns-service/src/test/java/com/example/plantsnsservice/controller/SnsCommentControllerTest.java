package com.example.plantsnsservice.controller;

import com.example.plantsnsservice.common.exception.CustomException;
import com.example.plantsnsservice.common.exception.ErrorCode;
import com.example.plantsnsservice.service.SnsCommentService;
import com.example.plantsnsservice.vo.request.SnsCommentRequestDto;
import com.example.plantsnsservice.vo.response.SnsCommentResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

@WebMvcTest(SnsCommentController.class)
@MockBean(JpaMetamodelMappingContext.class)
class SnsCommentControllerTest {
    @MockBean
    SnsCommentService snsCommentService;
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("댓글 생성 컨트롤러 테스트")
    void createSnsCommentTest() throws Exception{
        //given
        SnsCommentRequestDto snsCommentRequestDto = SnsCommentRequestDto.builder()
                .snsPostId(1L)
                .content("댓글 작성 테스트")
                .createdBy("Lee")
                .build();
        String stringJson = createStringJson(snsCommentRequestDto);

        SnsCommentResponseDto snsCommentResponseDto = SnsCommentResponseDto.builder()
                .id(1L)
                .content("댓글 작성 테스트")
                .createdBy("Lee")
                .build();
        given(snsCommentService.createComment(snsCommentRequestDto)).willReturn(snsCommentResponseDto);
        String expectedJson = createStringJson(snsCommentResponseDto);
        //then
        mvc.perform(post("/snsComment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringJson))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
                .andDo(print());
    }
    @Test
    @DisplayName("댓글 생성 컨트롤러 예외 테스트- 댓글 작성히 해당 게시글이 없을 경우")
    void createSnsCommentExceptionTest() throws Exception{
        //given
        SnsCommentRequestDto snsCommentRequestDto = SnsCommentRequestDto.builder()
                .snsPostId(1L)
                .content("댓글 작성 테스트")
                .createdBy("Lee")
                .build();
        String stringJson = createStringJson(snsCommentRequestDto);

        given(snsCommentService.createComment(any(SnsCommentRequestDto.class)))
                .willThrow(new CustomException(ErrorCode.SNS_POST_NOT_FOUND));
        //then
        mvc.perform(post("/snsComment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringJson))
                .andExpect(status().is(ErrorCode.SNS_POST_NOT_FOUND.getHttpStatus().value())) // 예상되는 HTTP 상태 코드 검증
                .andExpect(jsonPath("$.message").value(ErrorCode.SNS_POST_NOT_FOUND.getDetail()))
                .andDo(print());
    }
    @Test
    @DisplayName("댓글 조회 컨트롤러 테스트")
    void getSnsCommentTest() throws Exception{
        //given
        SnsCommentResponseDto parentComment= SnsCommentResponseDto.builder()
                .id(1L)
                .snsPostId(1L)
                .content("댓글 작성 테스트")
                .createdBy("Lee")
                .parent(null)
                .build();
        SnsCommentResponseDto childComment= SnsCommentResponseDto.builder()
                .id(2L)
                .snsPostId(1L)
                .content("댓글 작성 테스트2")
                .createdBy("Lee")
                .parent(null)
                .build();
        List<SnsCommentResponseDto> snsCommentList = new ArrayList<>();

        snsCommentList.add(parentComment);
        snsCommentList.add(childComment);
        String expectedJson = createStringJson(snsCommentList);


        given(snsCommentService.findCommentListByPostId(1L)).willReturn(snsCommentList);
        //then
        mvc.perform(get("/snsComment/{snsPostId}", 1L))
                .andExpect(content().json(expectedJson))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 삭제 컨트롤러 테스트")
    void deleteSnsCommentTest() throws Exception {
        //then
        mvc.perform(delete("/snsComment/{snsCommentId}", 1L))
                .andExpect(status().isOk())
                .andDo(print());
    }

    public String createStringJson(Object dto) throws JsonProcessingException {
        return mapper.writeValueAsString(dto);
    }

}