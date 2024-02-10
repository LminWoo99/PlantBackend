package com.example.plantchatservice.client;

import com.example.plantchatservice.dto.member.MemberDto;
import com.example.plantchatservice.dto.vo.ResponseTradeBoardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 마이크로서비스 간의 호출을 위한 feignclient
 *
 */
@FeignClient(name="plant-service")
public interface PlantServiceClient {
    @GetMapping("/api/list/{id}")
    ResponseEntity<ResponseTradeBoardDto> boardContent(@PathVariable(value = "id") Long id);

    @GetMapping("/api/findId")
    ResponseEntity<MemberDto> findById(@RequestParam Long id);
}
