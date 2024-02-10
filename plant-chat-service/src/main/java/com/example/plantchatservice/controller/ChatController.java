package com.example.plantchatservice.controller;

import com.example.plantchatservice.dto.chat.Message;
import com.example.plantchatservice.dto.member.MemberDto;
import com.example.plantchatservice.dto.vo.ChatRequestDto;
import com.example.plantchatservice.dto.vo.ChatRoomResponseDto;
import com.example.plantchatservice.service.ChatRoomService;
import com.example.plantchatservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;
    @PostMapping("/chatroom")
    public ResponseEntity<HttpStatus> createChatRoom(@RequestBody @Valid final ChatRequestDto requestDto,@RequestParam Long memberNo,
                                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
        }

        // 채팅방을 만들어준다.
        chatService.makeChatRoom(memberNo, requestDto);

        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    // 채팅방 리스트 조회
    @GetMapping("/chatroom")
    public ResponseEntity<List<ChatRoomResponseDto>> chatRoomList(@RequestParam(value = "tradeBoardNo", required = false) Long tradeBoardNo,
                                                                  @RequestParam Long memberNo) {
        List<ChatRoomResponseDto> chatList = chatService.getChatList(memberNo, tradeBoardNo);
        return ResponseEntity.ok(chatList);
    }

    @MessageMapping("/message")
    public void sendMessage(@Valid Message message, @RequestParam Long memberNo) {
        chatService.sendMessage(message, memberNo);
    }
    // 채팅방 접속 끊기
    @PostMapping("/chatroom/{chatroomNo}")
    public ResponseEntity<HttpStatus> disconnectChat(@PathVariable("chatroomNo") Long chatroomNo,
                                                            @RequestParam("nickname") String nickname) {
        chatRoomService.disconnectChatRoom(chatroomNo, nickname);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

}
