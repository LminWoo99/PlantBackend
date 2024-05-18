package com.example.plantchatservice.chat;

import com.example.plantchatservice.GenerateMockToken;
import com.example.plantchatservice.dto.chat.Message;
import com.example.plantchatservice.dto.redis.ChatRoom;
import com.example.plantchatservice.dto.vo.ChatRequestDto;
import com.example.plantchatservice.dto.vo.ChatResponseDto;
import com.example.plantchatservice.dto.vo.ChatRoomResponseDto;
import com.example.plantchatservice.dto.vo.ChattingHistoryResponseDto;
import com.example.plantchatservice.service.chat.ChatRoomService;
import com.example.plantchatservice.service.chat.ChatService;
import com.example.plantchatservice.testUser.WithMockCustomAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import static org.hamcrest.Matchers.is;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(controllers = ChatController.class)
public class ChatControllerTest extends MvcTestBasic {
    @MockBean
    ChatService chatService;

    @MockBean
    ChatRoomService chatRoomService;


    @Test
    @DisplayName("채팅방 생성 테스트")
    @WithMockCustomAccount
    void createChatRoomTest() throws Exception {
        ChatRequestDto chatRequestDto = new ChatRequestDto(1, 3);
        String requestJson = createStringJson(chatRequestDto);
        mvc.perform(post("/chatroom")
                        .headers(GenerateMockToken.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                        .andExpect(status().isOk())
                       .andDo(print());
    }

    @Test
    @DisplayName("채팅 내역 리스트 조회 테스트")
    @WithMockCustomAccount
    void chattingListTest() throws Exception {
        Integer roomNo = 1;
        ChatResponseDto chat1 = new ChatResponseDto("dssdx-13fd-dfasdf", roomNo, 1, "minu", "chat", "안녕하세요 :) 거래 가능할까요?",
                LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli(),
                1, true
        );

        ChatResponseDto chat2 = new ChatResponseDto("dssdx-23fd-dfasdff", roomNo, 1, "minu", "chat", "네 거래 가능합니다!",
                LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli(),
                0, true
        );


        List<ChatResponseDto> chatList = List.of(chat1, chat2);
        ChattingHistoryResponseDto chattingHistoryResponseDto = new ChattingHistoryResponseDto("mw310@naver.com", chatList);

        given(chatService.getChattingList(any(), any())).willReturn(chattingHistoryResponseDto);

        mvc.perform(get("/chatroom/{roomNo}", roomNo).headers(GenerateMockToken.getToken())
                        .param("memberNo", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatList", hasSize(2)))
                .andExpect(jsonPath("$.chatList[0].content", is("안녕하세요 :) 거래 가능할까요?")))
                .andExpect(jsonPath("$.chatList[1].content", is("네 거래 가능합니다!")));



    }

    @Test
    @DisplayName("채팅방 리스트 조회 테스트")
    @WithMockCustomAccount
    void chatRoomList() throws Exception {
        ChatRoomResponseDto.Participant participant = new ChatRoomResponseDto.Participant("이민우", "minu");
        ChatRoomResponseDto.LatestMessage latestMessage = ChatRoomResponseDto.LatestMessage.builder()
                .context("안녕하세요 :)")
                .sendAt(LocalDateTime.now())
                .build();
        ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto(1, 3, 4, 12
                , LocalDateTime.now());
        chatRoomResponseDto.setUnReadCount(10L);
        chatRoomResponseDto.setLatestMessage(latestMessage);
        chatRoomResponseDto.setParticipant(participant);

        ChatRoomResponseDto.Participant participant2 = new ChatRoomResponseDto.Participant("user3", "test");
        ChatRoomResponseDto.LatestMessage latestMessage2 = ChatRoomResponseDto.LatestMessage.builder()
                .context("오랜만이네요:)")
                .sendAt(LocalDateTime.now())
                .build();
        ChatRoomResponseDto chatRoomResponseDto2 = new ChatRoomResponseDto(2, 5, 23, 43
                , LocalDateTime.now());
        chatRoomResponseDto2.setUnReadCount(30L);
        chatRoomResponseDto2.setLatestMessage(latestMessage2);
        chatRoomResponseDto.setParticipant(participant2);

        List<ChatRoomResponseDto> chatRoomlist = List.of(chatRoomResponseDto, chatRoomResponseDto2);

        given(chatService.getChatList(any(), any())).willReturn(chatRoomlist);


        mvc.perform(get("/chatroom")
                        .headers(GenerateMockToken.getToken())
                        .param("tradeBoardNo", "1")
                        .param("memberNo", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tradeBoardNo", is(12)))
                .andExpect(jsonPath("$[0].unReadCount", is(10)))
                .andExpect(jsonPath("$[1].tradeBoardNo", is(43)))
                .andExpect(jsonPath("$[1].unReadCount", is(30)));
    }



    @Test
    @DisplayName("채팅방 접속 끊기 테스트")
    @WithMockCustomAccount
    void disconnectChatTest() throws Exception{
        ChatRoom chatRoom = new ChatRoom(1, 1, "minu");

        mvc.perform(post("/chatroom/{chatRoomNo}", chatRoom.getChatroomNo()).headers(GenerateMockToken.getToken())
                        .param("chatroomNo", "1")
                        .param("username", "minu"))
                        .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("알림 보내기 테스트")
    @WithMockCustomAccount
    void sendNotificationTest() throws Exception {
        Message requestMessage = Message.builder()
                .contentType("chat")
                .chatNo(1)
                .tradeBoardNo(2)
                .content("거래 가능할까요?")
                .build();

        Message responseMessage = Message.builder()
                .contentType("chat")
                .chatNo(1)
                .tradeBoardNo(2)
                .content("가능합니다!")
                .id("asc2-xcad-3d4f-fsdc")
                .senderName("minu")
                .senderNo(1)
                .sendTime(LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli())
                .readCount(0)
                .senderEmail("mw310@naver.com")
                .build();
        String stringJson = createStringJson(requestMessage);
        given(chatService.sendNotificationAndSaveMessage(any())).willReturn(responseMessage);

        mvc.perform(post("/chatroom/notification").contentType(MediaType.APPLICATION_JSON)
                        .content(stringJson))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content", is("가능합니다!")))
                        .andExpect(jsonPath("$.senderName", is("minu")))
                        .andExpect(jsonPath("$.senderEmail", is("mw310@naver.com")))
                        .andDo(print());

    }

    @Test
    @DisplayName("채팅방 존재하는지 확인 테스트")
    @WithMockCustomAccount
    void existChatRoomBySellerTest() throws Exception {
        given(chatService.existChatRoomBySeller(2, 1)).willReturn(true);

        mvc.perform(get("/chatroom/exist/seller").headers(GenerateMockToken.getToken())
                        .param("tradeBoardNo", "2")
                        .param("memberNo", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andDo(print());


    }

}