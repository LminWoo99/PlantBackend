package com.example.plantchatservice.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTradeBoardDto {
    private Long id;
    private String title;
    private String content;
    private String createBy;
    private Long memberId;
    private int view;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int price;
    private int goodCount;
    private String buyer;


}
