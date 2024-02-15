package com.example.plantchatservice.controller;

import com.example.plantchatservice.dto.chat.Message;
import com.example.plantchatservice.dto.vo.ChatRequestDto;
import com.example.plantchatservice.dto.vo.ChatRoomResponseDto;
import com.example.plantchatservice.dto.vo.ChattingHistoryResponseDto;
import com.example.plantchatservice.service.chat.ChatRoomService;
import com.example.plantchatservice.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;
    @PostMapping("/chatroom")
    public ResponseEntity<HttpStatus> createChatRoom(@RequestBody @Valid final ChatRequestDto requestDto,@RequestParam Integer memberNo,
                                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
        }

        // 채팅방을 만들어준다.
        chatService.makeChatRoom(memberNo, requestDto);

        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    // 채팅내역 조회
    @GetMapping("/chatroom/{roomNo}")
    public ResponseEntity<ChattingHistoryResponseDto> chattingList(@PathVariable("roomNo") Integer roomNo, @RequestParam Integer memberNo) {
        ChattingHistoryResponseDto chattingList = chatService.getChattingList(roomNo, memberNo);
        return ResponseEntity.ok().body(chattingList);
    }

    // 채팅방 리스트 조회
    @GetMapping("/chatroom")
    public ResponseEntity<List<ChatRoomResponseDto>> chatRoomList(@RequestParam(value = "tradeBoardNo", required = false) Integer tradeBoardNo,
                                                                  @RequestParam Integer memberNo) {
        List<ChatRoomResponseDto> chatList = chatService.getChatList(memberNo, tradeBoardNo);
        return ResponseEntity.ok(chatList);
    }

    @MessageMapping("/message")
    public void sendMessage(@Valid Message message, @Header("Authorization") final String accessToken) {
        log.info("message 호출" + message);
        chatService.sendMessage(message, accessToken);
    }
    @MessageExceptionHandler
    @SendTo("/error")
    public String handleException(Exception e) {

        return "WebSocket 메시지 핸들러에서 예외가 발생했습니다: " + e;
    }
    // 채팅방 접속 끊기
    @PostMapping("/chatroom/{chatroomNo}")
    public ResponseEntity<HttpStatus> disconnectChat(@PathVariable("chatroomNo") Integer chatroomNo,
                                                            @RequestParam("nickname") String nickname) {
        chatRoomService.disconnectChatRoom(chatroomNo, nickname);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
    @PostMapping("/chatroom/notification")
    public ResponseEntity<Message> sendNotification(@Valid @RequestBody Message message) {
        Message savedMessage = chatService.sendNotificationAndSaveMessage(message);
        return ResponseEntity.ok(savedMessage);
    }

}
