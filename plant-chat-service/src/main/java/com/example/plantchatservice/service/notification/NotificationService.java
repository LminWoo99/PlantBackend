package com.example.plantchatservice.service.notification;

import com.example.plantchatservice.client.PlantServiceClient;
import com.example.plantchatservice.common.exception.ErrorCode;
import com.example.plantchatservice.domain.NotifiTypeEnum;
import com.example.plantchatservice.domain.Notification;
import com.example.plantchatservice.dto.member.MemberDto;
import com.example.plantchatservice.dto.notification.NotificationResponse;
import com.example.plantchatservice.repository.notification.EmitterRepository;
import com.example.plantchatservice.repository.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 1000L * 60 * 30 ;// 30분
    public static final String PREFIX_URL = "http://localhost:3000/";
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;


    private final PlantServiceClient plantServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;
    /**
     * SSE 연결 메서드
     * @param : MemberDto memberDto, String lastEnventId
     */
    @Transactional
    public SseEmitter subscribe(MemberDto memberDto, String lastEventId) {
        Long memberNo = memberDto.getId();
        //Emitter map에 저장하기 위한 key 생성
        String id = memberNo + "_" + System.currentTimeMillis();
        // SseEmitter map에 저장
        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        log.info("emitter add: {}", emitter);
        // emitter의 완료 또는 타임아웃 Event가 발생할 경우, 해당 emitter를 삭제
        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        // 503 에러를 방지하기 위한 더미 Event 전송
        sendToClient(emitter, id, "EventStream Created. [userId=" + memberNo + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (!lastEventId.isEmpty()) {
            // id에 해당하는 cache 찾음
            Map<String, Object> events = emitterRepository.findAllCacheStartWithId(String.valueOf(memberNo));

            //미수신한 Event 목록 전송
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }
        return emitter;

    }
    /**
     * 알림 전송 메서드
     * 알림 받을 유저 SseEmitter 가져와서 알림 전송
     * @param : MemberDto sender, MemberDto receiver, NotifiTypeEnum type, String resource, String content
     */
    @Transactional
    public void send(MemberDto sender, MemberDto receiver, NotifiTypeEnum type, String resource, String content) {
        // 알림 생성
        Notification notification = Notification.builder()
                .senderNo(sender.getId().intValue())
                .receiverNo(receiver.getId().intValue())
                .typeEnum(type)
                .url(PREFIX_URL + type.getPath() + resource)
                .content(content)
                .isRead(false)
                .isDel(false)
                .build();
        // SseEmitter 캐시 조회를 위해 key의 prefix 생성
        String id = String.valueOf(notification.getReceiverNo());

        //알림 저장
        notificationRepository.save(notification);

        // 로그인 한 유저의 SseEmitter 모두 가져오기
        Map<String, SseEmitter> sseEmitterMap = emitterRepository.findAllStartWithById(id);
        sseEmitterMap.forEach(
                (key, emitter) -> {
                    //캐시 저장(유실한 데이터를 처리)
                    emitterRepository.saveCache(key, notification);
                    //데아터 전송
                    sendToClient(emitter, key, NotificationResponse.toDto(notification));
                }
        );
    }
    /**
     * 클라이언트에 SSE + 알림 데이터 전송 메서드
     * @param : SseEmitter emitter, String id, Object data
     */
    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException ex) {
            emitterRepository.deleteById(id);
            log.error("--- SSE 연결 오류 ----", ex);
        }
    }

    /**
     * 로그인한 멤버 알림 전체 조회
     * 조회용 메서드 => (readOnly = true)
     *
     * @param : MemberDto memberDto
     */
    public List<NotificationResponse> findAllById(MemberDto memberDto) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        ResponseEntity<MemberDto> member = circuitBreaker.run(() -> plantServiceClient.findById(memberDto.getId()),
                throwable -> ResponseEntity.ok(null));
        // 채팅의 마지막 알림 조회
        List<Notification> chat
                = notificationRepository.findChatByReceiver(member.getBody().getId().intValue());

        return chat.stream()
                .map(NotificationResponse::toDto)
                .sorted(Comparator.comparing(NotificationResponse::getId).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 일림 읽음 처리 메서드
     * @param : Long id
     */
    public void readNotification(Long id) {
        Notification notification = getNotification(id);
        notification.read();
    }
    /**
     * 일림 삭제 처리 메서드
     * @param : Long id
     */
    public void deleteNotification(Long[] idList) {
        for (Long id : idList) {
            Notification notification = getNotification(id);
            notificationRepository.delete(notification);
        }


    }

    //== 개별 알림 조회 ==//
    private Notification getNotification(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(ErrorCode::throwNotificationNotFound);
    }
}
