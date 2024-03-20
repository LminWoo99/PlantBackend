package com.example.plantchatservice.service.chat;

import com.example.plantchatservice.chat.ChatController;
import com.example.plantchatservice.chat.MvcTestBasic;
import com.example.plantchatservice.domain.Chat;
import com.example.plantchatservice.dto.vo.ChatRequestDto;
import com.example.plantchatservice.dto.vo.ResponseTradeBoardDto;
import com.example.plantchatservice.repository.chat.ChatRepository;
import com.example.plantchatservice.repository.mongo.MongoChatRepository;
import com.example.plantchatservice.service.AggregationSender;
import com.example.plantchatservice.service.notification.NotificationService;
import com.example.plantchatservice.testUser.WithMockCustomAccount;
import com.google.common.base.Function;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@AutoConfigureMockMvc
@WebMvcTest(controllers = ChatService.class)
class ChatServiceTest extends MvcTestBasic {
    @MockBean
    ChatRepository chatRepository;
    @MockBean
    NotificationService notificationService;
    @MockBean
    ChatRoomService chatRoomService;

    @MockBean
    CircuitBreakerFactory circuitBreakerFactory;
    @MockBean
    CircuitBreaker circuitBreaker;
    @InjectMocks
    private ChatService chatService;

    @Test
    @DisplayName("채팅방 만들기 테스트")
    @WithMockCustomAccount
    void makeChatRoomTest() throws Exception {
        //given
        ChatRequestDto chatRequestDto = new ChatRequestDto(1, 1);
        ResponseTradeBoardDto responseTradeBoardDto = new ResponseTradeBoardDto();
        Chat chat = new Chat(1, 1, 2, 11, LocalDateTime.now());
        given(chatRepository.existChatRoomByBuyer(anyInt(), anyInt(), anyInt())).willReturn(false);
        given(circuitBreakerFactory.create(anyString())).willReturn(circuitBreaker);
        given(chatRepository.save(any(Chat.class))).willReturn(chat);
        //when

        Chat resultChat = chatService.makeChatRoom(1, chatRequestDto);

        //then
        assertEquals(chat, resultChat);
    }

    @Test
    void getChatList() {
    }

    @Test
    void getChattingList() {
    }

    @Test
    void sendMessage() {
    }

    @Test
    void sendNotificationAndSaveMessage() {
    }

    @Test
    void updateMessage() {
    }

    @Test
    void updateCountAllZero() {
    }

    @Test
    void countUnReadMessage() {
    }

    @Test
    void existChatRoomBySeller() {
    }

    @Test
    void deleteChatRoom() {
    }

    @Test
    void deleteChatting() {
    }
}