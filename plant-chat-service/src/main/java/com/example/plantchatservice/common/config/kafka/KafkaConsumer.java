package com.example.plantchatservice.common.config.kafka;

import com.example.plantchatservice.service.chat.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ChatService chatService;
    /**
     * plant-service 게시글에 속한 채팅 데이터 kafka를 통해 삭제 메서드
     *
     * @param : MemberDto memberDto, ChatRequestDto requestDto
     */
    @KafkaListener(topics = "deletePost")
    public void deleteChat(String kafkaMessage) {
        log.info("Kafka Message : ->" + kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
            });
            //TradeBoard id값 가져오기
            Integer tradeBoardNo = (Integer) map.get("id");
            if (tradeBoardNo == null) {
                throw new IllegalArgumentException("TradeBoard ID is missing in the Kafka message");
            }
            chatService.deleteChatRoom(tradeBoardNo);

        } catch (JsonProcessingException e) {
            log.error("Error parsing Kafka message: {}", kafkaMessage, e);
        } catch (IllegalArgumentException e) {
            log.error("Validation error for Kafka message: {}", kafkaMessage, e);
        } catch (Exception e) {
            log.error("Error processing Kafka message: {}", kafkaMessage, e);
        }

    }
}