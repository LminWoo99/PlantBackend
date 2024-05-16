package com.example.plantchatservice.service.notification;

import com.example.plantchatservice.common.util.KafkaUtil;
import com.example.plantchatservice.dto.chat.Message;
import com.example.plantchatservice.dto.notification.NotificationEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationReceiver {
    private final NotificationService notificationService;
    @KafkaListener(topics = KafkaUtil.KAFKA_NOTIFICATION, containerFactory = "kafkaNotificationContainerFactory")
    public void receiveNotification(NotificationEventDto notificationEventDto) {
        log.info("======알림 전송 type: {}======", notificationEventDto.getType());

        notificationService.send(notificationEventDto.getSenderNo(), notificationEventDto.getReceiverNo(),
                    notificationEventDto.getType(), notificationEventDto.getResource(), notificationEventDto.getContent()
        );

    }

}
