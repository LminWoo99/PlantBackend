package com.example.plantchatservice.chat;

import com.example.plantchatservice.domain.Chat;
import com.example.plantchatservice.dto.chat.Message;
import com.example.plantchatservice.dto.vo.ChatRequestDto;
import com.example.plantchatservice.dto.vo.ChatRoomResponseDto;
import com.example.plantchatservice.dto.vo.ChattingHistoryResponseDto;
import com.example.plantchatservice.dto.vo.StatusResponseDto;
import com.example.plantchatservice.service.chat.ChatRoomService;
import com.example.plantchatservice.service.chat.ChatService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "채팅방 생성", description = "채팅방 생성 할 수 있는 API")
    public ResponseEntity<StatusResponseDto> createChatRoom(@RequestBody @Valid final ChatRequestDto requestDto, @RequestParam(required = false) Integer memberNo,
                                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(StatusResponseDto.addStatus(400));
        }

        // 채팅방을 만들어준다.
        Chat chat = chatService.makeChatRoom(memberNo, requestDto);
        return ResponseEntity.ok(StatusResponseDto.addStatus(200, chat));
    }

    @GetMapping("/chatroom/{roomNo}")
    @Operation(summary = "채팅 내역 조회", description = "채팅방 no 기준 모든 채팅 내역 조회할 수 있는 API")
    public ResponseEntity<ChattingHistoryResponseDto> chattingList(@PathVariable("roomNo") Integer roomNo, @RequestParam(required = false) Integer memberNo) {
        ChattingHistoryResponseDto chattingList = chatService.getChattingList(roomNo, memberNo);
        return ResponseEntity.ok().body(chattingList);
    }

    @GetMapping("/chatroom")
    @Operation(summary = "채팅방 리스트 조회", description = "채팅방 리스트 조회할 수 있는 API")
    public ResponseEntity<List<ChatRoomResponseDto>> chatRoomList(@RequestParam(value = "tradeBoardNo", required = false) Integer tradeBoardNo,
                                                                  @RequestParam(value = "memberNo", required = false) Integer memberNo) {
        List<ChatRoomResponseDto> chatList = chatService.getChatList(memberNo, tradeBoardNo);
        return ResponseEntity.ok(chatList);
    }

    @MessageMapping("/message")
    public void sendMessage(@Valid Message message, @Header("Authorization") final String accessToken) {
        chatService.sendMessage(message, accessToken);
    }

    @MessageExceptionHandler
    @SendTo("/error")
    public String handleException(Exception e) {
        return "WebSocket 메시지 핸들러에서 예외가 발생했습니다: " + e;
    }

    @PostMapping("/chatroom/{chatroomNo}")
    @Operation(summary = "채팅방 접속 끊기", description = "채팅방 접속 끊을수 있는 API, 채팅방 접속 끊은 시 redis 채팅방 인원 제거")
    public ResponseEntity<HttpStatus> disconnectChat(@PathVariable("chatroomNo") Integer chatroomNo,
                                                     @RequestParam(value = "username", required = false) String username) {

        chatRoomService.disconnectChatRoom(chatroomNo, username);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @PostMapping("/chatroom/notification")
    @Operation(summary = "알림 전송", description = "SSE 알림 전송할 수 있는 API")
    public ResponseEntity<Message> sendNotification(@Valid @RequestBody Message message) {
        Message savedMessage = chatService.sendNotificationAndSaveMessage(message);
        return ResponseEntity.ok(savedMessage);
    }
    @GetMapping("/chatroom/exist/seller")
    @Operation(summary = "판매자 채팅방 조회", description = "판매자가 참가한 채팅방 존재하는지 조회할 수 있는 API")
    public Boolean existChatRoomBySeller(@RequestParam Integer tradeBoardNo, @RequestParam Integer memberNo) {
        return chatService.existChatRoomBySeller(tradeBoardNo, memberNo);
    }
}
