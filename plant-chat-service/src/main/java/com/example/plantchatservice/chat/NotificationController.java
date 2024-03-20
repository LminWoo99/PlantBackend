package com.example.plantchatservice.chat;

import com.example.plantchatservice.dto.notification.NotificationDeleteRequest;
import com.example.plantchatservice.dto.notification.NotificationResponse;
import com.example.plantchatservice.dto.vo.StatusResponseDto;
import com.example.plantchatservice.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/notification")
@Slf4j
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * @title 로그인 한 유저 SSE 연결
     * @param : String lastEventId,String jwtToken
     **/
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId,
            @RequestHeader("Authorization") String jwtToken) {
        SseEmitter emitter = notificationService.subscribe(lastEventId, jwtToken);

        return ResponseEntity.ok(emitter);
    }

    /**
     * @title 로그인 한 유저의 모든 알림 조회
     * @param : Long memberNo(현재 접속한 멤버)
     **/
    @GetMapping("/all")
    public ResponseEntity<List<NotificationResponse>> notifications(Long memberNo) {
        return ResponseEntity.ok(notificationService.findAllById(memberNo));
    }

    /**
     * @title 알림 읽음 상태만 변경
     * @param : Long id(알림 id)
     **/
    @PatchMapping("/checked/{id}")
    public ResponseEntity<StatusResponseDto> readNotification(@PathVariable("id") Long id) {
        notificationService.readNotification(id);
        return ResponseEntity.ok(StatusResponseDto.success());
    }

    /**
     * @title 알림 삭제
     * @param : NotificationDeleteRequest request
     **/
    @DeleteMapping
    public ResponseEntity<StatusResponseDto> deleteNotification(@RequestBody NotificationDeleteRequest request) {
        notificationService.deleteNotification(request.getIdList());
        return ResponseEntity.ok(StatusResponseDto.success());
    }


}
