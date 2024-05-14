package com.example.plantchatservice.service.chat;

import com.example.plantchatservice.GenerateMockToken;
import com.example.plantchatservice.client.PlantServiceClient;
import com.example.plantchatservice.common.util.TokenHandler;
import com.example.plantchatservice.domain.Chat;
import com.example.plantchatservice.domain.NotifiTypeEnum;
import com.example.plantchatservice.domain.mongo.Chatting;
import com.example.plantchatservice.dto.chat.Message;
import com.example.plantchatservice.dto.member.MemberDto;
import com.example.plantchatservice.dto.vo.*;
import com.example.plantchatservice.repository.chat.ChatRepository;
import com.example.plantchatservice.repository.mongo.MongoChatRepository;
import com.example.plantchatservice.service.AggregationSender;
import com.example.plantchatservice.service.notification.NotificationService;
import com.example.plantchatservice.testUser.WithMockCustomAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest{
    @Mock
    private ChatRepository chatRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private ChatRoomService chatRoomService;
    @Mock
    private PlantServiceClient plantServiceClient;
    @Mock
    private MongoTemplate mongoTemplate;
    @Mock
    private MessageSender sender;

    @Mock
    private AggregationSender aggregationSender;
    @Mock
    private TokenHandler tokenHandler;
    @Mock
    private MongoChatRepository mongoChatRepository;
    @Mock
    private CircuitBreakerFactory circuitBreakerFactory;

    @InjectMocks
    ChatService chatService;



    @Test
    @DisplayName("채팅방 만들기 테스트")
    @WithMockCustomAccount
    void makeChatRoomTest() throws Exception {
        //given
        CircuitBreaker circuitBreaker = mock(CircuitBreaker.class);
        when(circuitBreakerFactory.create(anyString())).thenReturn(circuitBreaker);

        ChatRequestDto chatRequestDto = new ChatRequestDto(1, 1);

        TradeBoardResponseDto tradeBoardResponseDto = new TradeBoardResponseDto();

        ResponseEntity<TradeBoardResponseDto> mockResponse = ResponseEntity.ok(tradeBoardResponseDto);
        Chat expectChat = new Chat(1, 1, 2, 11, LocalDateTime.now());

        when(chatRepository.existChatRoomByBuyer(any(), any(), any())).thenReturn(false);
        when(circuitBreaker.run(any(), any())).thenReturn((mockResponse));
        when(chatRepository.save(any(Chat.class))).thenReturn((expectChat));
//        doNothing().when(aggregationSender).send(any(), any());
        //when
        Chat resultChat = chatService.makeChatRoom(1, chatRequestDto);

        //then
        assertThat(expectChat).isEqualTo(resultChat);
    }
    @Test
    @DisplayName("이미 거래된 채팅방 예외 테스트")
    @WithMockCustomAccount
    void makeChatRoomExceptionTest() throws Exception {
        //given
        CircuitBreaker circuitBreaker = mock(CircuitBreaker.class);
        when(circuitBreakerFactory.create(anyString())).thenReturn(circuitBreaker);

        ChatRequestDto chatRequestDto = new ChatRequestDto(1, 1);

        TradeBoardResponseDto tradeBoardResponseDto = new TradeBoardResponseDto();
        ResponseEntity<TradeBoardResponseDto> mockResponse = ResponseEntity.ok(tradeBoardResponseDto);

        when(circuitBreaker.run(any(), any())).thenReturn(mockResponse);

        //when
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            // 테스트하고자 하는 메서드 호출
            chatService.makeChatRoom(2, chatRequestDto);
        });
        String expectedMessage = "현재 거래가능 상태가 아닙니다.";
        String actualMessage = exception.getMessage();
        //then
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    @DisplayName("채팅방 리스트 조회 테스트")
    void getChatListTest() {
        //given
        List<ChatRoomResponseDto> mockChatRoomList = new ArrayList<>();

        ChatRoomResponseDto.Participant participant = new ChatRoomResponseDto.Participant("이민우", "minu");
        ChatRoomResponseDto.LatestMessage latestMessage = ChatRoomResponseDto.LatestMessage.builder()
                .context("안녕하세요 :)")
                .sendAt(LocalDateTime.now())
                .build();
        ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto(1, 3, 4, 12
                , LocalDateTime.now());
        chatRoomResponseDto.setUnReadCount(0);
        chatRoomResponseDto.setLatestMessage(latestMessage);
        chatRoomResponseDto.setParticipant(participant);

        mockChatRoomList.add(chatRoomResponseDto);

        ResponseEntity<MemberDto> mockResponseEntity = ResponseEntity.ok(new MemberDto(3L , "minu", "ee", "이민우", "mw310@~"));

        when(chatRepository.getChattingList(anyInt(), anyInt())).thenReturn(mockChatRoomList);
        when(plantServiceClient.findById(anyLong())).thenReturn(mockResponseEntity);
        Page<Chatting> mockPage = mock(Page.class);

        when(mongoChatRepository.findByChatRoomNoOrderBySendDateDesc(anyInt(), any(PageRequest.class))).thenReturn(mockPage);
        //when
        List<ChatRoomResponseDto> result = chatService.getChatList(1, 2);
        //then
        assertThat(1).isEqualTo(result.get(0).getChatNo());
    }

    @Test
    @DisplayName("채팅 메세지 내역 조회")
    void getChattingListTest() {
        //given
        CircuitBreaker circuitBreaker = mock(CircuitBreaker.class);
        when(circuitBreakerFactory.create(anyString())).thenReturn(circuitBreaker);
        Integer roomNo = 1;
        Chatting chat1 = new Chatting("dssdx-13fd-dfasdf", roomNo, 1, "minu", "chat", "안녕하세요 :) 거래 가능할까요?",
                LocalDateTime.now(), 1
        );

        Chatting chat2 = new Chatting("dssdx-23fd-dfasdff", roomNo, 1, "minu", "chat", "네 거래 가능합니다!",
                LocalDateTime.now(), 0
        );


        List<Chatting> chatList = List.of(chat1, chat2);
        List<ChatResponseDto> chattingList = chatList.stream().map(chat -> new ChatResponseDto(chat, 1)).collect(Collectors.toList());
        ChattingHistoryResponseDto chattingHistoryResponseDto = new ChattingHistoryResponseDto("mw310@naver.com", chattingList);

        ResponseEntity<MemberDto> mockResponseEntity = ResponseEntity.ok(new MemberDto(3L , "minu", "ee", "이민우", "pass", "mw310@~"));

        when(circuitBreaker.run(any(), any())).thenReturn(mockResponseEntity);

        when(mongoChatRepository.findByChatRoomNo(anyInt())).thenReturn(chatList);

        //when
        ChattingHistoryResponseDto resultChattingList = chatService.getChattingList(roomNo, 1);

        //then
        assertThat(resultChattingList.getChatList().size()).isEqualTo(2);
        assertThat(chattingHistoryResponseDto.getChatList().get(1).getContent()).isEqualTo(resultChattingList.getChatList().get(1).getContent());

    }

    @Test
    @DisplayName("모든 유저가 참여중일때 동작 확인")
    void sendMessageTest() {
        //given
        Message requestMessage = Message.builder()
                .contentType("chat")
                .chatNo(1)
                .tradeBoardNo(2)
                .senderName("이민우")
                .content("거래 가능할까요?")
                .build();

        ResponseEntity<MemberDto> mockResponseEntity = ResponseEntity.ok(new MemberDto(3L , "minu", "ee", "이민우", "pass", "mw310@~"));
        when(plantServiceClient.findByUsername(any())).thenReturn(mockResponseEntity);
        when(chatRoomService.isAllConnected(1)).thenReturn(true);

        //when
        chatService.sendMessage(requestMessage, GenerateMockToken.getToken().toString());
        //then
        assertThat(requestMessage.getReadCount()).isEqualTo(0);

    }

    @Test
    @DisplayName("상대가 읽지 않은 경우에 알림 전송되는지")
    void sendNotificationAndSaveMessageTest() {
        //given
        Message requestMessage = Message.builder()
                .contentType("chat")
                .chatNo(1)
                .senderNo(1)
                .tradeBoardNo(2)
                .senderEmail("mw310@naver.com")
                .content("거래 가능할까요?")
                .build();
        CircuitBreaker circuitBreaker = mock(CircuitBreaker.class);
        when(circuitBreakerFactory.create(anyString())).thenReturn(circuitBreaker);

        ResponseEntity<MemberDto> mockResponseEntity = ResponseEntity.ok(new MemberDto(2L , "minu", "ee", "이민우", "pass", "mw310@~"));

        when(circuitBreaker.run(any(), any())).thenReturn(mockResponseEntity);
        when(chatRoomService.isAllConnected(1)).thenReturn(false);
        when(chatRepository.getReceiverMember(1, 1)).thenReturn(2);
        //when
        chatService.sendNotificationAndSaveMessage(requestMessage);
        //then
        verify(notificationService, times(1)).send(mockResponseEntity.getBody().getId().intValue(),mockResponseEntity.getBody().getId().intValue(), NotifiTypeEnum.CHAT,"1/2","거래 가능할까요?");

    }
    @Test
    @DisplayName("상대가 읽은 경우에 알림 전송 안되는지")
    void sendNotificationAndSaveMessageReadTest() {
        //given
        Message requestMessage = Message.builder()
                .contentType("chat")
                .chatNo(1)
                .senderNo(1)
                .tradeBoardNo(2)
                .senderEmail("mw310@naver.com")
                .content("거래 가능할까요?")
                .build();
        CircuitBreaker circuitBreaker = mock(CircuitBreaker.class);
        when(circuitBreakerFactory.create(anyString())).thenReturn(circuitBreaker);

        ResponseEntity<MemberDto> mockResponseEntity = ResponseEntity.ok(new MemberDto(2L , "minu", "ee", "이민우",  "xxxx@naver.com"));

        when(circuitBreaker.run(any(), any())).thenReturn(mockResponseEntity);
        when(chatRoomService.isAllConnected(1)).thenReturn(true);

        //when
        chatService.sendNotificationAndSaveMessage(requestMessage);
        //then
        verify(notificationService, times(0)).send(mockResponseEntity.getBody().getId().intValue(),mockResponseEntity.getBody().getId().intValue(), NotifiTypeEnum.CHAT,"1/2","거래 가능할까요?");

    }
    @Test
    @DisplayName("채팅을 보낸사람이 아닐 경우 채팅이 저장이 안되는지 예외 테스트")
    void sendNotificationAndSaveMessageNotEqualEmailTest() {
        //given
        Message requestMessage = Message.builder()
                .contentType("chat")
                .chatNo(1)
                .senderNo(1)
                .tradeBoardNo(2)
                .senderEmail("mw310@naver.com")
                .content("거래 가능할까요?")
                .readCount(1)
                .build();
        Chatting chatting = requestMessage.convertEntity();
        CircuitBreaker circuitBreaker = mock(CircuitBreaker.class);
        when(circuitBreakerFactory.create(anyString())).thenReturn(circuitBreaker);

        ResponseEntity<MemberDto> mockResponseEntity = ResponseEntity.ok(new MemberDto(2L , "minu", "ee", "이민우", "pass", "xxx@"));

        when(circuitBreaker.run(any(), any())).thenReturn(mockResponseEntity);
        when(chatRoomService.isAllConnected(1)).thenReturn(true);
        //when
        chatService.sendNotificationAndSaveMessage(requestMessage);
        //then
        verify(mongoChatRepository, times(0)).save(chatting);

    }


    @Test
    @DisplayName("판매자가 참가한 채팅방 존재 유무 테스트")
    void existChatRoomBySellerTest() {
        //given
        Integer tradeBoardNo = 2;
        Integer memberNo = 1;
        when(chatRepository.existChatRoomBySeller(tradeBoardNo, memberNo)).thenReturn(true);

        //when
        Boolean result = chatService.existChatRoomBySeller(tradeBoardNo, memberNo);
        //then
        assertThat(result).isTrue();
    }


}