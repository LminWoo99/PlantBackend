package com.example.plantpayservice.controller;

import com.example.plantpayservice.service.PaymentService;
import com.example.plantpayservice.vo.request.PaymentRequestDto;
import com.example.plantpayservice.vo.response.PaymentResponseDto;
import com.example.plantpayservice.vo.response.StatusResponseDto;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

    @Value("${iamport.key}")
    private String restApiKey;

    @Value("${iamport.secret}")
    private String restApiSecret;

    private IamportClient iamportClient;
    private final PaymentService paymentService;

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
    @Operation(summary = "식구페이 머니 충전", description = "카카오페이를 통해 충전 가능")
    public ResponseEntity<StatusResponseDto> chargePayMoney(
            @RequestBody PaymentRequestDto paymentRequestDto,
            @RequestHeader("Idempotency-Key") String idempotencyKey) {
        log.info("충전 요청 Idempotency-Key: {}", idempotencyKey);
        StatusResponseDto statusResponseDto = paymentService.chargePayMoney(paymentRequestDto, idempotencyKey);
        return ResponseEntity.ok().body(statusResponseDto);
    }

    @PostMapping("/payMoney/validation")
    @Operation(summary = "식구페이 머니로 거래 API", description = "구매자는 보유 금액-거래 금액, 판매자는 보유 금액+거래 금액")
    public ResponseEntity<StatusResponseDto> validTransaction(@RequestBody PaymentRequestDto paymentRequestDto) {
        StatusResponseDto statusResponseDto = paymentService.validTransaction(paymentRequestDto);
        return ResponseEntity.ok().body(statusResponseDto);
    }

    @PatchMapping("/payMoney/refund")
    @Operation(summary = "식구페이 머니 환불 API", description = "계좌로 환불 (테스트 환경)")
    public ResponseEntity<StatusResponseDto> refundPayMoney(
            @RequestBody PaymentRequestDto paymentRequestDto,
            @RequestHeader("Idempotency-Key") String idempotencyKey) {
        log.info("환불 요청 Idempotency-Key: {}", idempotencyKey);
        StatusResponseDto statusResponseDto = paymentService.refundPayMoney(paymentRequestDto, idempotencyKey);
        return ResponseEntity.ok().body(statusResponseDto);
    }

}