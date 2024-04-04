package com.example.plantpayservice.controller;


import com.example.plantpayservice.service.PaymentService;
import com.example.plantpayservice.vo.request.PaymentRequestDto;
import com.example.plantpayservice.vo.response.PaymentResponseDto;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PaymentController {
    @Value("${iamport.key}")
    private  String restApiKey;
    @Value("${iamport.secret}")
    private String restApiSecret;
    private IamportClient iamportClient;
    private final PaymentService paymentService;
    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(restApiKey, restApiSecret);

    }
    //iamport api 연동
    @PostMapping("/verifyIamport/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(@PathVariable("imp_uid") String imp_uid) throws IOException {
        log.info("info : " + iamportClient.getAuth());
        return iamportClient.paymentByImpUid(imp_uid);

    }
    //식구페이 머니 조회
    @GetMapping("/payMoney/{memberNo}")
    public ResponseEntity<PaymentResponseDto> getPayMoney(@PathVariable("memberNo") Integer memberNo) {
        PaymentResponseDto paymentResponseDto = paymentService.getPayMoney(memberNo);
        return ResponseEntity.ok().body(paymentResponseDto);

    }
    //식구페이 머니 충전
    @PostMapping("/payMoney/charge")
    public void chargePayMoney(@RequestBody PaymentRequestDto paymentRequestDto) {
        paymentService.chargePayMoney(paymentRequestDto);

    }

    //식구페이로 거래하기
    @PostMapping("/payMoney/trade")
    public ResponseEntity<HttpStatus> tradePayMoney(@RequestBody PaymentRequestDto paymentRequestDto, @RequestParam(value = "sellerNo", required = false) Integer sellerNo) {
        paymentService.tradePayMoney(paymentRequestDto, sellerNo);
        return ResponseEntity.ok().body(HttpStatus.ACCEPTED);

    }
    //식구페이 머니 환불
    @PatchMapping("/payMoney/refund")
    public void refundPayMoney(@RequestBody PaymentRequestDto paymentRequestDto) {
        paymentService.refundPayMoney(paymentRequestDto);
    }


}
