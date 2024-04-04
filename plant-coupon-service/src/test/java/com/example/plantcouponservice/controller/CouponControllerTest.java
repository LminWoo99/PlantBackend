package com.example.plantcouponservice.controller;

import com.example.plantcouponservice.service.CouponService;
import com.example.plantcouponservice.vo.CouponRequestDto;
import com.example.plantcouponservice.vo.CouponResponseDto;
import com.example.plantcouponservice.vo.StatusResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import scala.Int;

import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(controllers = CouponController.class)
class CouponControllerTest {
    @MockBean
    CouponService couponService;

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("쿠폰 적용 컨트롤러 테스트")
    void applyCouponTest() throws Exception {
        //given
        CouponRequestDto couponRequestDto = new CouponRequestDto(1, 3000);
        StatusResponseDto statusResponseDto = StatusResponseDto.success();
        given(couponService.applyCoupon(couponRequestDto)).willReturn(statusResponseDto);
        String stringJson = createStringJson(couponRequestDto);
        //when, then
        mvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringJson))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("쿠폰 적용 예외 테스트- 한 계정당 2개이상 발급 받을 경우")
    void applyCouponExceptionTest() throws Exception {
        //given
        CouponRequestDto couponRequestDto = new CouponRequestDto(1, 3000);
        StatusResponseDto statusResponseDto = StatusResponseDto.addStatus(409);
        given(couponService.applyCoupon(couponRequestDto)).willReturn(statusResponseDto);
        String stringJson = createStringJson(couponRequestDto);
        //when, then
        mvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringJson))
                .andExpect(status().isConflict())
                .andDo(print());
    }
    @Test
    @DisplayName("쿠폰 소진 오류 테스트")
    void applyCouponExhaustedTest() throws Exception {
        //given
        CouponRequestDto couponRequestDto = new CouponRequestDto(1, 3000);
        StatusResponseDto statusResponseDto = StatusResponseDto.addStatus(429);
        given(couponService.applyCoupon(couponRequestDto)).willReturn(statusResponseDto);
        String stringJson = createStringJson(couponRequestDto);
        //when, then
        mvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringJson))
                .andExpect(status().isTooManyRequests())
                .andDo(print());
    }
    @Test
    @DisplayName("쿠폰 조회 테스트")
    void getCouponTest() throws Exception {
        //given
        Integer memberNo = 1;
        CouponResponseDto couponResponseDto1 = CouponResponseDto.builder()
                .couponNo(1L)
                .memberNo(1)
                .discountPrice(3000)
                .build();
        CouponResponseDto couponResponseDto2 = CouponResponseDto.builder()
                .couponNo(2L)
                .memberNo(1)
                .discountPrice(3000)
                .build();
        List<CouponResponseDto> couponResponseDtoList = new ArrayList<>();
        couponResponseDtoList.add(couponResponseDto1);
        couponResponseDtoList.add(couponResponseDto2);
        given(couponService.getCoupon(memberNo)).willReturn(couponResponseDtoList);
        //when,the,
        mvc.perform(get("/coupon/{memberNo}", memberNo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].couponNo", is(1)))
                .andExpect(jsonPath("$[0].memberNo", is(1)))
                .andExpect(jsonPath("$[0].discountPrice", is(3000)))
                .andExpect(jsonPath("$[1].couponNo", is(2)))
                .andExpect(jsonPath("$[1].memberNo", is(1)))
                .andExpect(jsonPath("$[1].discountPrice", is(3000)))
                .andDo(print());

    }

    @Test
    void useCoupon() throws Exception {
        //given
        Integer memberNo = 1;
        Long couponNo = 1L;
        CouponResponseDto couponResponseDto1 = CouponResponseDto.builder()
                .couponNo(1L)
                .memberNo(1)
                .discountPrice(3000)
                .build();
        given(couponService.useCoupon(memberNo, couponNo)).willReturn(couponResponseDto1);
        //when, then
        mvc.perform(post("/coupon/used")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("memberNo", memberNo.toString())
                        .param("couponNo", couponNo.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.couponNo", is(couponNo.intValue())))
                .andExpect(jsonPath("$.memberNo", is(memberNo)))
                .andExpect(jsonPath("$.discountPrice", is(3000)))
                .andDo(print());
    }
    public String createStringJson(Object dto) throws JsonProcessingException {
        return mapper.writeValueAsString(dto);
    }
}