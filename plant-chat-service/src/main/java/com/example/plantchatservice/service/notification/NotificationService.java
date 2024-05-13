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
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
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
    private static final Long DEFAULT_TIMEOUT = 1000L * 60 * 29 ;// 29분
    public static final String PREFIX_URL = "https://sikguhaza.site/";
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;


    private final PlantServiceClient plantServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;
    /**
     * SSE 연결 메서드
     * @param : MemberDto memberDto, String lastEnventId
     */
    @Transactional
    public SseEmitter subscribe(String lastEventId, String jwtToken) {
        ResponseEntity<MemberDto> joinMember = plantServiceClient.getJoinMember(jwtToken);
        Integer memberNo = joinMember.getBody().getId().intValue();
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
    public void send(Integer senderNo, Integer receiverNo, NotifiTypeEnum type, String resource, String content) {
        // 알림 생성
        Notification notification = Notification.builder()
                .senderNo(senderNo)
                .receiverNo(receiverNo)
                .typeEnum(type)
                .url(PREFIX_URL + type.getPath() + resource)
                .content(content)
                .isRead(false)
                .isDel(false)
                .build();
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        //보낸 사람 이름 찾기 위해 feignClient를 통해 호출
        ResponseEntity<MemberDto> findMember = circuitBreaker.run(() -> plantServiceClient.findById(senderNo.longValue()),
                throwable -> ResponseEntity.ok(null));

        notification.setSenderName(findMember.getBody().getNickname());
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
            log.info("event : " + data);
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
            log.info("event call: " + emitter);
        } catch (IOException ex) {
            emitterRepository.deleteById(id);
            log.error("--- SSE 연결 오류 ----", ex);
        }
    }

    /**
     * 로그인한 멤버 알림 전체 조회
     * 조회용 메서드 => (readOnly = true)
     * @param : MemberDto memberDto
     */
    public List<NotificationResponse> findAllById(Long memberNo) {
        // 채팅의 마지막 알림 조회
        List<Notification> chat
                = notificationRepository.findChatByReceiver(memberNo.intValue());

        return chat.stream()
                .map(NotificationResponse::toDto)
                .sorted(Comparator.comparing(NotificationResponse::getId).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 일림 읽음 처리 메서드
     * @param : Long id
     */
    @Transactional
    public void readNotification(Long id) {
        notificationRepository.updateIsReadById(id);
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
