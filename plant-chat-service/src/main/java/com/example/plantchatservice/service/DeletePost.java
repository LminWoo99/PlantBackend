package com.example.plantchatservice.service;

import com.example.plantchatservice.dto.vo.TradeBoardResponseDto;
import com.example.plantchatservice.service.chat.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeletePost {

    private final ChatService chatService;
    /**
     * plant-service 게시글에 속한 채팅 데이터 kafka를 통해 삭제 메서드
     * @param : MemberDto memberDto, ChatRequestDto requestDto
     */
    @KafkaListener(topics = "deletePost", containerFactory = "kafkaDeletePostContainerFactory")
    public void deleteChat(String kafkaMessage, Long tradeBoardNo) {
        log.info("Kafka Message : ->" + kafkaMessage);
        if (tradeBoardNo == null) {
            throw new IllegalArgumentException("TradeBoard ID is missing in the Kafka message");
        }
        chatService.deleteChatRoom(tradeBoardNo.intValue());

    }
}