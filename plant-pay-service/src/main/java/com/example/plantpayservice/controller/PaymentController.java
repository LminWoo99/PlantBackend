package com.example.plantpayservice.controller;


import com.example.plantpayservice.service.PaymentSaga;
import com.example.plantpayservice.service.PaymentService;
import com.example.plantpayservice.vo.request.PaymentRequestDto;
import com.example.plantpayservice.vo.response.PaymentResponseDto;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.v3.oas.annotations.Operation;
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
    private final PaymentSaga paymentSaga;
    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(restApiKey, restApiSecret);

    }
    @PostMapping("/verifyIamport/{imp_uid}")
    @Operation(summary = "iamport api 연동")
    public IamportResponse<Payment> paymentByImpUid(@PathVariable("imp_uid") String imp_uid) throws IOException {
        log.info("info : " + iamportClient.getAuth());
        return iamportClient.paymentByImpUid(imp_uid);

    }
    @GetMapping("/payMoney/{memberNo}")
    @Operation(summary = "식구페이 머니 조회", description = "식구 페이 머니 조회할 수 있는 API")
    public ResponseEntity<PaymentResponseDto> getPayMoney(@PathVariable("memberNo") Integer memberNo) {
        PaymentResponseDto paymentResponseDto = paymentService.getPayMoney(memberNo);
        return ResponseEntity.ok().body(paymentResponseDto);

    }
    @PostMapping("/payMoney/charge")
    @Operation(summary = "식구페이 머니 충전", description = "구매자는 보유 금액-거래 금액, 판매자는 보유 금액+거래 금액 ")
    public ResponseEntity<?> chargePayMoney(@RequestBody PaymentRequestDto paymentRequestDto) {
        paymentService.chargePayMoney(paymentRequestDto);

        return ResponseEntity.ok().build();
    }

    //식구페이로 거래하기
    @PostMapping("/payMoney/trade")
    public ResponseEntity<?> tradePayMoney(@RequestBody PaymentRequestDto paymentRequestDto) {
        //분산 트랜잭션 SAGA Pattern
        paymentSaga.startSaga(paymentRequestDto);

        return ResponseEntity.ok().build();
    }
    //식구페이 머니 환불
    @PatchMapping("/payMoney/refund")
    public void refundPayMoney(@RequestBody PaymentRequestDto paymentRequestDto) {
        paymentService.refundPayMoney(paymentRequestDto);
    }


}
