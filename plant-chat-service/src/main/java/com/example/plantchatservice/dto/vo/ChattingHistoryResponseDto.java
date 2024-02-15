package com.example.plantchatservice.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class ChattingHistoryResponseDto {
    private String email;
    private List<ChatResponseDto> chatList;
}
