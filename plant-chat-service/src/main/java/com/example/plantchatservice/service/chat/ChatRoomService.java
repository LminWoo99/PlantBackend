package com.example.plantchatservice.service.chat;

import com.example.plantchatservice.dto.redis.ChatRoom;
import com.example.plantchatservice.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    /**
     * 채팅방 연결 메서드
     * @param : Long chatRoomNo , String username
     */
    @Transactional
    public void connectChatRoom(Integer chatRoomNo, String username) {
        ChatRoom chatRoom = ChatRoom.builder()
                .username(username)
                .chatroomNo(chatRoomNo)
                .build();

        chatRoomRepository.save(chatRoom);
    }
    /**
     * 채팅방 연결 끊기
     * @param : Integer chatRoomNo , String username
     */
    @Transactional
    public void disconnectChatRoom(Integer chatRoomNo, String username) {
        ChatRoom chatRoom = chatRoomRepository.findByChatroomNoAndUsername(chatRoomNo, username)
                .orElseThrow(IllegalStateException::new);
        chatRoomRepository.delete(chatRoom);
    }
    /**
     * 채팅방 정원 2명 찼는지 확인 메서드
     * @param : Integer chatRoomNo
     */
    public boolean isAllConnected(Integer chatRoomNo) {
        List<ChatRoom> connectedList = chatRoomRepository.findByChatroomNo(chatRoomNo);
        return connectedList.size() == 2;
    }
    /**
     * 채팅방에 1명 연결됐는지 확인 메서드
     * @param : Integer chatRoomNo
     */
    public boolean isConnected(Integer chatRoomNo) {
        List<ChatRoom> connectedList = chatRoomRepository.findByChatroomNo(chatRoomNo);
        return connectedList.size() == 1;
    }
}