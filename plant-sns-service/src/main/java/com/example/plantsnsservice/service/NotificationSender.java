package com.example.plantsnsservice.service;

import com.example.plantsnsservice.domain.NotifiTypeEnum;
import com.example.plantsnsservice.vo.request.NotificationEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationSender {
    private final KafkaTemplate<String, NotificationEventDto> kafkaTemplate;

    public void send(String topic, NotificationEventDto notificationEventDto) {
        kafkaTemplate.send(topic, notificationEventDto);
    }
}
