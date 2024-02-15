package com.example.plantchatservice.common.config.interceptor;

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
    /**
     * class에 @AllArgsConstructor 붙이지 말고
     * 생성자 선언해서 @Builder
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // StompCommand에 따라서 로직을 분기해서 처리하는 메서드를 호출한다.
        String username = verifyAccessToken(getAccessToken(accessor));
        log.info("StompAccessor = {}", accessor);
        handleMessage(accessor.getCommand(), accessor, username);
        return message;
    }

    private void handleMessage(StompCommand stompCommand, StompHeaderAccessor accessor, String username) {
        log.info(stompCommand.toString());
        switch (stompCommand) {

            case CONNECT:
                log.info("wow");
                connectToChatRoom(accessor, username);
                break;
            case SUBSCRIBE:
            case SEND:
                verifyAccessToken(getAccessToken(accessor));
                break;
        }
    }
    private String getAccessToken(StompHeaderAccessor accessor) {
        return accessor.getFirstNativeHeader("Authorization");
    }
    private String verifyAccessToken(String accessToken) {
        if (!tokenHandler.verifyToken(accessToken)) {
            throw new IllegalStateException("토큰이 만료되었습니다.");
        }

        return tokenHandler.getUid(accessToken);
    }

    private void connectToChatRoom(StompHeaderAccessor accessor, String username) {
        // 채팅방 번호를 가져온다.
        Integer chatRoomNo = getChatRoomNo(accessor);

        // 채팅방 입장 처리 -> Redis에 입장 내역 저장
        chatRoomService.connectChatRoom(chatRoomNo, username);
//        // 읽지 않은 채팅을 전부 읽음 처리
        chatService.updateCountAllZero(chatRoomNo, username);
        // 현재 채팅방에 접속중인 인원이 있는지 확인한다.
        boolean isConnected = chatRoomService.isConnected(chatRoomNo);

        if (isConnected) {
            chatService.updateMessage(username, chatRoomNo);
        }
    }


    private Integer getChatRoomNo(StompHeaderAccessor accessor) {
        return
                Integer.valueOf(
                        Objects.requireNonNull(
                                accessor.getFirstNativeHeader("chatRoomNo")
                        ));
    }
}