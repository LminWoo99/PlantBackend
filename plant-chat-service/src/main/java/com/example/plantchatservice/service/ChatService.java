package com.example.plantchatservice.service;


import com.example.plantchatservice.client.PlantServiceClient;
import com.example.plantchatservice.domain.Chat;
import com.example.plantchatservice.dto.aggregation.AggregationDto;
import com.example.plantchatservice.dto.aggregation.AggregationTarget;
import com.example.plantchatservice.dto.chat.Message;
import com.example.plantchatservice.dto.member.MemberDto;
import com.example.plantchatservice.dto.vo.ChatRequestDto;
import com.example.plantchatservice.dto.vo.ChatRoomResponseDto;
import com.example.plantchatservice.dto.vo.ResponseTradeBoardDto;
import com.example.plantchatservice.repository.ChatRepository;
import com.example.plantchatservice.repository.mongo.MongoChatRepository;
import com.example.plantchatservice.util.KafkaUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final MongoChatRepository mongoChatRepository;
    private final MessageSender sender;
//    private final AggregationSender aggregationSender;
    private final MongoTemplate mongoTemplate;
    private final ChatRoomService chatRoomService;
    private final PlantServiceClient plantServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;
    /**
     * 채팅방 생성 메서드
     * FeignCLient를 통해 plant-service에서 거래가능 여부 확인
     * @param : MemberDto memberDto, ChatRequestDto requestDto
     */
    @Transactional
    public Chat makeChatRoom(Long memberNo, ChatRequestDto requestDto) {
        CircuitBreaker circuitbreaker = circuitBreakerFactory.create("circuitbreaker");
        // 채팅을 걸려고 하는 거래글이 거래 가능 상태인지 조회해본다.
        ResponseEntity<ResponseTradeBoardDto> tradeBoardDto = circuitbreaker.run(() ->
                plantServiceClient.boardContent(requestDto.getTradeBoardNo()),
                throwable -> ResponseEntity.ok(null));


        // 조회해온 거래글 상태가 거래완료 이라면 분양이 불가능한 상태이다.
        if (tradeBoardDto.getBody().getStatus().equals("거래완료")) {
            throw new IllegalStateException("현재 거래가능 상태가 아닙니다.");
        }

        Chat chat = Chat.builder()
                .tradeBoardNo(requestDto.getTradeBoardNo())
                .createMember(requestDto.getCreateMember())
                .joinMember(memberNo)
                .regDate(LocalDateTime.now())
                .build();

        Chat savedChat = chatRepository.save(chat);

        // 채팅방 카운트 증가
/*        AggregationDto aggregationDto = AggregationDto
                .builder()
                .isIncrease("true")
                .target(AggregationTarget.CHAT)
                .tradeBoardNo(requestDto.getTradeBoardNo())
                .build();

        aggregationSender.send(KafkaUtil.KAFKA_AGGREGATION, aggregationDto);*/
        return savedChat;
    }

    public List<ChatRoomResponseDto> getChatList(Long memberNo, Long tradeBoardNo) {
        List<ChatRoomResponseDto> chatRoomList = chatRepository.getChattingList(memberNo, tradeBoardNo);

        //Participant 채워야됨(username)
            chatRoomList
                    .forEach(chatRoomDto -> {
                        //param으로 넘어온 멤버가 채팅 만든 멤버일 경우 => Participant에 참가한 멤버
                        //param으로 넘어온 멤버가 채팅방에 참가한 멤버일 경우 => Participant에 채팅방 민든 멤버
                        ResponseEntity<MemberDto> byId = plantServiceClient.findById(chatRoomDto.getCreateMember());
                        if (byId.getBody().getId().equals(chatRoomDto.getCreateMember())) {
                            ResponseEntity<MemberDto> memberDtoResponse = plantServiceClient.findById(chatRoomDto.getJoinMember());

                            chatRoomDto.setParticipant(new ChatRoomResponseDto.Participant(memberDtoResponse.getBody().getUsername(), memberDtoResponse.getBody().getNickname()));
                        } else {
                            ResponseEntity<MemberDto> memberDtoResponse = plantServiceClient.findById(chatRoomDto.getCreateMember());
                            chatRoomDto.setParticipant(new ChatRoomResponseDto.Participant(memberDtoResponse.getBody().getUsername(), memberDtoResponse.getBody().getNickname()));
                        }

//                        // 채팅방별로 읽지 않은 메시지 개수를 셋팅
//                        long unReadCount = countUnReadMessages(chatRoomDto.getChatNo(), memberDto.getId());
//                        chatRoomDto.setUnReadCount(unReadCount);
//
//                        // 채팅방별로 마지막 채팅내용과 시간을 셋팅
//                        Page<Chatting> chatting =
//                                mongoChatRepository.findByChatRoomNoOrderBySendDateDesc(chatRoomDto.getChatNo(), PageRequest.of(0, 1));
//                        if (chatting.hasContent()) {
//                            Chatting chat = chatting.getContent().get(0);
//                            ChatRoomResponseDto.LatestMessage latestMessage = ChatRoomResponseDto.LatestMessage.builder()
//                                    .context(chat.getContent())
//                                    .sendAt(chat.getSendDate())
//                                    .build();
//                            chatRoomDto.setLatestMessage(latestMessage);
//                        }
                    });

        return chatRoomList;
    }

    public void sendMessage(Message message, Long id) {
        // member id로 조화
        ResponseEntity<MemberDto> memberDto = plantServiceClient.findById(id);

        // 채팅방에 모든 유저가 참여중인지 확인한다.
        boolean isConnectedAll = chatRoomService.isAllConnected(message.getChatNo());
        // 1:1 채팅이므로 2명 접속시 readCount 0, 한명 접속시 1
        Integer readCount = isConnectedAll ? 0 : 1;
        // message 객체에 보낸시간, 보낸사람 memberNo, 닉네임을 셋팅해준다.
        message.setSendTimeAndSender(LocalDateTime.now(), memberDto.getBody().getId(), memberDto.getBody().getNickname(), readCount);
        // 메시지를 전송한다.
        sender.send(KafkaUtil.KAFKA_TOPIC, message);
    }

    public void updateMessage(String email, Long chatRoomNo) {
        Message message = Message.builder()
                .contentType("notice")
                .chatNo(chatRoomNo)
                .content(email + " 님이 돌아오셨습니다.")
                .build();

        sender.send(KafkaUtil.KAFKA_TOPIC, message);
    }

}
