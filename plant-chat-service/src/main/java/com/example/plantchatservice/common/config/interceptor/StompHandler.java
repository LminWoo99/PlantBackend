package com.example.plantchatservice.common.config.interceptor;

import com.example.plantchatservice.common.exception.ErrorCode;
import com.example.plantchatservice.service.chat.ChatRoomService;
import com.example.plantchatservice.service.chat.ChatService;
import com.example.plantchatservice.common.util.TokenHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final ChatRoomService chatRoomService;
    private final ChatService chatService;
    private final TokenHandler tokenHandler;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // StompCommand에 따라서 로직을 분기해서 처리하는 메서드를 호출한다.
        String accessToken = getAccessToken(accessor);
        if (accessToken == null) {
            throw ErrorCode.missingTokenException();
        }
        String username = verifyAccessToken(accessToken);
        log.info("StompAccessor = {}", accessor);
        handleMessage(accessor.getCommand(), accessor, username);
        return message;
    }

    private void handleMessage(StompCommand stompCommand, StompHeaderAccessor accessor, String username) {
        switch (stompCommand) {
            case CONNECT:
                connectToChatRoom(accessor, username);
                break;
            case SUBSCRIBE:
            case SEND:
                tokenHandler.getUid(getAccessToken(accessor));
                break;
            case DISCONNECT:
                chatRoomService.disconnectChatRoom(getChatRoomNo(accessor), username);
                break;
        }
    }

    private String getAccessToken(StompHeaderAccessor accessor) {
        return accessor.getFirstNativeHeader("Authorization");
    }

    private String verifyAccessToken(String accessToken) {
        return tokenHandler.getUid(accessToken);
    }

    private void connectToChatRoom(StompHeaderAccessor accessor, String username) {
        Integer chatRoomNo = getChatRoomNo(accessor);
        Integer tradeBoardNo = getTradeBoardNo(accessor);
        // 채팅방 입장 처리 후 Redis에 입장 내역 저장
        chatRoomService.connectChatRoom(chatRoomNo, username, tradeBoardNo);
        // 읽지 않은 채팅을 전부 읽음 처리
        chatService.updateCountAllZero(chatRoomNo, username);
        // 현재 채팅방에 접속중인 인원 확인
        boolean isConnected = chatRoomService.isConnected(chatRoomNo);

        if (isConnected) {
            chatService.updateMessage(username, chatRoomNo);
        }
    }

    private Integer getChatRoomNo(StompHeaderAccessor accessor) {
        return Integer.valueOf(Objects.requireNonNull(accessor.getFirstNativeHeader("chatRoomNo")));
    }

    private Integer getTradeBoardNo(StompHeaderAccessor accessor) {
        return Integer.valueOf(Objects.requireNonNull(accessor.getFirstNativeHeader("tradeBoardNo")));
    }
}
