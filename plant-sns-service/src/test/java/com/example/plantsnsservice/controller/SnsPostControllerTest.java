package com.example.plantsnsservice.controller;

import com.example.plantsnsservice.common.exception.CustomException;
import com.example.plantsnsservice.common.exception.ErrorCode;
import com.example.plantsnsservice.common.handler.ExceptionResponseHandler;
import com.example.plantsnsservice.service.SnsPostService;
import com.example.plantsnsservice.vo.request.SnsPostRequestDto;
import com.example.plantsnsservice.vo.response.SnsPostResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;

@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = {SnsPostController.class, ExceptionResponseHandler.class})
class SnsPostControllerTest {
    @MockBean
    SnsPostService snsPostService;
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;


    @Test
    @DisplayName("게시글 생성 컨트롤러 단위 테스트")
    void createSnsPostTest() throws Exception {
        //given
        SnsPostRequestDto snsPostRequestDto=SnsPostRequestDto.builder()
                .id(1L)
                .snsPostTitle("sns 게시글 테스트")
                .snsPostContent("테스트")
                .memberNo(1L)
                .createdAt(LocalDateTime.now())
                .build();

        String stringJson = createStringJson(snsPostRequestDto);

        mvc.perform(post("/snsPost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringJson))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 리스트 조회 테스트")
    void getSnsPostListTest() throws Exception {
        //given
        SnsPostResponseDto snsPostRequestDto=SnsPostResponseDto.builder()
                .id(1L)
                .snsPostTitle("sns 게시글 테스트")
                .snsPostContent("테스트")
                .memberNo(1L)
                .build();
        //given
        SnsPostResponseDto snsPostRequestDto1=SnsPostResponseDto.builder()
                .id(2L)
                .snsPostTitle("sns 게시글 테스트2")
                .snsPostContent("테스트")
                .memberNo(2L)
                .build();
        List<SnsPostResponseDto> snsPostResponseDtoList = new ArrayList<>();
        snsPostResponseDtoList.add(snsPostRequestDto);
        snsPostResponseDtoList.add(snsPostRequestDto1);

        given(snsPostService.getSnsPostList()).willReturn(snsPostResponseDtoList);
        String expectedJson = "[{\"id\":1,\"snsPostTitle\":\"sns 게시글 테스트\",\"snsPostContent\":\"테스트\",\"memberNo\":1,\"createdAt\":null,\"snsLikesCount\":null,\"snsViewsCount\":null},{\"id\":2,\"snsPostTitle\":\"sns 게시글 테스트2\",\"snsPostContent\":\"테스트\",\"memberNo\":2,\"createdAt\":null,\"snsLikesCount\":null,\"snsViewsCount\":null}]";

        //then
        mvc.perform(get("/snsPosts"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
                .andDo(print());

    }
    @Test
    @DisplayName("SNS 게시글 해시 태그 기준으로 조회")
    void getSnsPostsByHashTagTest() throws Exception{
        //given
        String hashTagName = "#LeeMinWoo";
        List hashTagNameList = new ArrayList<>();

        hashTagNameList.add(hashTagName);

        SnsPostResponseDto snsPostRequestDto=SnsPostResponseDto.builder()
                .id(1L)
                .snsPostTitle("sns 게시글 테스트")
                .snsPostContent("테스트")
                .hashTags(hashTagNameList)
                .memberNo(1L)
                .build();
        SnsPostResponseDto snsPostRequestDto1=SnsPostResponseDto.builder()
                .id(2L)
                .snsPostTitle("sns 게시글 테스트2")
                .snsPostContent("테스트")
                .hashTags(hashTagNameList)
                .memberNo(2L)
                .build();
        List<SnsPostResponseDto> expectedList = new ArrayList<>();

        expectedList.add(snsPostRequestDto);
        expectedList.add(snsPostRequestDto1);
        String stringJson = createStringJson(expectedList);
        given(snsPostService.findAllByHashTag(hashTagName)).willReturn(expectedList);

        //when&then
        mvc.perform(get("/snsPosts/{hashTagName}", hashTagName))
                .andExpect(status().isOk())
                .andExpect(content().json(stringJson))
                .andDo(print());
    }
    @Test
    @DisplayName("SNS 게시글 업데이트 실패 - 게시글을 찾을 수 없음")
    void updateSnsPost_NotFound() throws Exception {
        // given
        SnsPostRequestDto snsPostRequestDto=SnsPostRequestDto.builder()
                .id(1L)
                .snsPostTitle("sns 게시글 테스트")
                .snsPostContent("테스트")
                .memberNo(1L)
                .createdAt(LocalDateTime.now())
                .build();
        String snsPostJson = createStringJson(snsPostRequestDto);

        doThrow(new CustomException(ErrorCode.SNS_POST_NOT_FOUND)).when(snsPostService).updateSnsPost(snsPostRequestDto);
        // when & then
        mvc.perform(patch("/snsPost")
                        .content(snsPostJson))
                .andExpect(status().is5xxServerError());

    }
    @Test
    @DisplayName("SNS 게시글 업데이트- 테스트")
    void updateSnsPostTest() throws Exception {
        //given
        SnsPostRequestDto snsPostRequestDto=SnsPostRequestDto.builder()
                        .id(1L)
                        .snsPostTitle("sns 게시글 테스트")
                        .snsPostContent("테스트")
                        .memberNo(1L)
                        .createdAt(LocalDateTime.now())
                        .build();
        String snsPostJson = createStringJson(snsPostRequestDto);
        // when & then
        mvc.perform(patch("/snsPost")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(snsPostJson))
                .andExpect(status().isOk());
    }
    public String createStringJson(Object dto) throws JsonProcessingException {
        return mapper.writeValueAsString(dto);
    }
}