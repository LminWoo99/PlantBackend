package com.example.plantpayservice.controller;

import com.example.plantpayservice.service.PaymentService;
import com.example.plantpayservice.vo.response.PaymentResponseDto;
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
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(controllers = PaymentController.class)
class PaymentControllerTest {
    @MockBean
    PaymentService paymentService;

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @Test
    @DisplayName("보유 페이머니 조회 단위 테스트")
    void getPayMoneyTest() throws Exception {
        //given
        PaymentResponseDto paymentResponseDto = new PaymentResponseDto(12000, 1);
        given(paymentService.getPayMoney(1)).willReturn(paymentResponseDto);
        String expectedJsonResponse = "{\"payMoney\":12000,\"memberNo\":1}";
        //when, then
        mvc.perform(get("/payMoney/{memberNo}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJsonResponse))
                .andDo(print());
    }
    @Test
    @DisplayName("보유 페이머니 조회 단위 테스트")
    void chargePayMoneyTest() throws Exception {
        //given
        PaymentResponseDto paymentResponseDto = new PaymentResponseDto(12000, 1);
        String stringJson = createStringJson(paymentResponseDto);

        //when, then
        mvc.perform(post("/payMoney/charge").contentType(MediaType.APPLICATION_JSON)
                        .content(stringJson))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("페이 머니로 거래 단위 테스트")
    void tradePayMoneyTest() throws Exception {
        //given
        PaymentResponseDto paymentResponseDto = new PaymentResponseDto(12000, 1);
        String stringJson = createStringJson(paymentResponseDto);
        //when, then
        mvc.perform(post("/payMoney/trade").contentType(MediaType.APPLICATION_JSON)
                        .content(stringJson)
                        .param("sellerNo", "2"))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("페이 머니로 거래 단위 테스트")
    void refundPayMoneyTest() throws Exception {
        //given
        PaymentResponseDto paymentResponseDto = new PaymentResponseDto(12000, 1);
        String stringJson = createStringJson(paymentResponseDto);


        //when, then
        mvc.perform(patch("/payMoney/refund").contentType(MediaType.APPLICATION_JSON)
                        .content(stringJson))
                .andExpect(status().isOk())
                .andDo(print());

    }
    public String createStringJson(Object dto) throws JsonProcessingException {
        return mapper.writeValueAsString(dto);
    }
}