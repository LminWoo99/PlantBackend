package com.example.plantchatservice.service.chat;


import com.example.plantchatservice.client.PlantServiceClient;
import com.example.plantchatservice.domain.Chat;
import com.example.plantchatservice.domain.NotifiTypeEnum;
import com.example.plantchatservice.domain.mongo.Chatting;
import com.example.plantchatservice.dto.aggregation.AggregationDto;
import com.example.plantchatservice.dto.aggregation.AggregationTarget;
import com.example.plantchatservice.dto.chat.Message;
import com.example.plantchatservice.dto.member.MemberDto;
import com.example.plantchatservice.dto.vo.*;
import com.example.plantchatservice.repository.chat.ChatRepository;
import com.example.plantchatservice.repository.mongo.MongoChatRepository;
import com.example.plantchatservice.common.util.KafkaUtil;
import com.example.plantchatservice.common.util.TokenHandler;
import com.example.plantchatservice.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final MongoChatRepository mongoChatRepository;
    private final MessageSender sender;
    private final AggregationSender aggregationSender;
    private final MongoTemplate mongoTemplate;
    private final ChatRoomService chatRoomService;
    private final PlantServiceClient plantServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final TokenHandler tokenHandler;

    private final NotificationService notificationService;

    /**
     * 채팅방 생성 메서드
     * 거래 게시글을 올리지 않은 사람만 호출하는 메서드
     * 구매 희망자만 채팅방을 생성 가능
     * FeignCLient를 통해 plant-service에서 거래가능 여부 확인
     * @param : MemberDto memberDto, ChatRequestDto requestDto
     */
    @Transactional
    public Chat makeChatRoom(Integer memberNo, ChatRequestDto requestDto) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        // 채팅을 걸려고 하는 거래글이 거래 가능 상태인지 조회해본다.
        ResponseEntity<TradeBoardResponseDto> tradeBoardDto = circuitBreaker.run(() ->
                        plantServiceClient.boardContent(requestDto.getTradeBoardNo().longValue()),
                throwable -> ResponseEntity.ok(null));

        // 조회해온 거래글 상태가 거래완료 이라면 거래가 불가능한 상태이다.
        if (tradeBoardDto.getBody().getStatus().equals("거래완료")) {
            throw new IllegalStateException("현재 거래가능 상태가 아닙니다.");
        }
        Integer tradeBoardNo = tradeBoardDto.getBody().getId().intValue();

        //이미 해당글 기준으로 채팅을 요청한 사람과 받는 사람이 일치할 경우 체크
        if (chatRepository.existChatRoomByBuyer(tradeBoardNo, requestDto.getCreateMember(), memberNo)) {
            Chat existedChat = chatRepository.findByTradeBoardNoAndChatNo(tradeBoardNo, requestDto.getCreateMember());
            return existedChat;

        }
        if (!chatRepository.existChatRoomByBuyer(tradeBoardNo, requestDto.getCreateMember(), memberNo)) {
            Chat chat = Chat.builder()
                    .tradeBoardNo(requestDto.getTradeBoardNo())
                    .createMember(requestDto.getCreateMember())
                    .joinMember(memberNo)
                    .regDate(LocalDateTime.now())
                    .build();

            Chat savedChat = chatRepository.save(chat);


            // 채팅방 카운트 증가
            AggregationDto aggregationDto = AggregationDto
                    .builder()
                    .isIncrease("true")
                    .target(AggregationTarget.CHAT)
                    .tradeBoardNo(requestDto.getTradeBoardNo())
                    .build();

            aggregationSender.send(KafkaUtil.KAFKA_AGGREGATION, aggregationDto);
            return savedChat;
        }
        throw new IllegalArgumentException("존재하지 않는 게시글 입니다");
    }
    /**
     * 채팅방 리스트 조회 메서드
     * FeignCLient를 통해 plant-service에서 유저 정보 조회후 채팅방 만든 사람인지 확인
     * mongodb에서 채팅 메세지 보낸 시간을 내림차순으로 정렬후 첫번째 값 마지막 메세지로 세팅
     * @param : Integer memberNo, Integer tradeBoardNo
     */
    @Transactional
    public List<ChatRoomResponseDto> getChatList(Integer memberNo, Integer tradeBoardNo) {
        List<ChatRoomResponseDto> chatRoomList = chatRepository.getChattingList(memberNo, tradeBoardNo);
        //Participant 채워야됨(username)
            chatRoomList
                    .forEach(chatRoomDto -> {
                        //param으로 넘어온 멤버가 채팅 만든 멤버일 경우 => Participant에 참가한 멤버
                        if (memberNo.equals(chatRoomDto.getCreateMember())) {
                            ResponseEntity<MemberDto> memberDtoResponse = plantServiceClient.findById(chatRoomDto.getJoinMember().longValue());

                            chatRoomDto.setParticipant(new ChatRoomResponseDto.Participant(memberDtoResponse.getBody().getUsername(), memberDtoResponse.getBody().getNickname()));
                        }
                        //param으로 넘어온 멤버가 채팅방에 참가한 멤버일 경우 => Participant에 채팅방 만든 멤버
                        if (!memberNo.equals(chatRoomDto.getCreateMember())){
                            ResponseEntity<MemberDto> memberDtoResponse = plantServiceClient.findById(chatRoomDto.getCreateMember().longValue());
                            chatRoomDto.setParticipant(new ChatRoomResponseDto.Participant(memberDtoResponse.getBody().getUsername(), memberDtoResponse.getBody().getNickname()));
                        }
//                      // 채팅방별로 읽지 않은 메시지 개수를 셋팅
                        long unReadCount = countUnReadMessage(chatRoomDto.getChatNo(), memberNo);
                        chatRoomDto.setUnReadCount(unReadCount);

                        // 채팅방별로 마지막 채팅내용과 시간을 셋팅
                        Page<Chatting> chatting =
                                mongoChatRepository.findByChatRoomNoOrderBySendDateDesc(chatRoomDto.getChatNo(), PageRequest.of(0, 1));
                        if (chatting.hasContent()) {
                            Chatting chat = chatting.getContent().get(0);
                            ChatRoomResponseDto.LatestMessage latestMessage = ChatRoomResponseDto.LatestMessage.builder()
                                    .context(chat.getContent())
                                    .sendAt(chat.getSendDate())
                                    .build();
                            chatRoomDto.setLatestMessage(latestMessage);
                        }
                    });

        return chatRoomList;
    }
    /**
     * 채팅 메세지 조회 메서드
     * 채팅 메세지 조회시 해당 메세지를 읽은 것이므로 메세지 읽음 처리도 진행
     * @param : Integer chatRoomNo, Integer memberNo
     */
    public ChattingHistoryResponseDto getChattingList(Integer chatRoomNo, Integer memberNo) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        // member id로 조화
        ResponseEntity<MemberDto> memberDto = circuitBreaker.run(() -> plantServiceClient.findById(memberNo.longValue()),
                throwable -> ResponseEntity.ok(null));

        updateCountAllZero(chatRoomNo, memberDto.getBody().getUsername());
        List<ChatResponseDto> chattingList = mongoChatRepository.findByChatRoomNo(chatRoomNo)
                .stream()
                .map(chat -> new ChatResponseDto(chat, memberNo)
                )
                .collect(Collectors.toList());

        return ChattingHistoryResponseDto.builder()
                .chatList(chattingList)
                .email(memberDto.getBody().getEmail())
                .build();
    }
    /**
     * 메세지 전송 메서드
     * jwt 토큰에서 username 추출
     * 카프카 토픽으로 메세 전송
     * @param : Message message, String accessToken
     */
    public void sendMessage(Message message, String accessToken) {
        // member id로 조화
        ResponseEntity<MemberDto> memberDto = plantServiceClient.findByUsername(tokenHandler.getUid(accessToken));

        // 채팅방에 모든 유저가 참여중인지 확인한다.
        boolean isConnectedAll = chatRoomService.isAllConnected(message.getChatNo());
        // 1:1 채팅이므로 2명 접속시 readCount 0, 한명 접속시 1
        Integer readCount = isConnectedAll ? 0 : 1;
        // message 객체에 보낸시간, 보낸사람 memberNo, 닉네임을 셋팅해준다.
        message.setSendTimeAndSender(LocalDateTime.now(), memberDto.getBody().getId().intValue(), memberDto.getBody().getNickname(), readCount);

        // 메시지를 전송한다.
        sender.send(KafkaUtil.KAFKA_TOPIC, message);
    }
    /**
     * 알림 전송 및 메세지 저장 메서드
     * FeignCLient를 통해 plant-service에서 메세지 보낸 유저 정보 조회
     * 알림은 상대방이 읽지 않은 경우만 전송
     * @param : Message message
     */
    @Transactional
    public Message sendNotificationAndSaveMessage(Message message) {

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        //메세지 저장과 알림 발송을 위해 메세지 보낸 회원 조회
        ResponseEntity<MemberDto> memberDto = circuitBreaker.run(() -> plantServiceClient.findById(message.getSenderNo().longValue()),
                throwable -> ResponseEntity.ok(null));
        // 상대방이 읽지 않은 경우에만 알림 전송
        if (!chatRoomService.isAllConnected(message.getChatNo())) {
            // 알람 전송을 위해 메시지를 받는 사람을 조회한다.
            Integer memberNo = chatRepository.getReceiverMember(message.getChatNo(), message.getSenderNo());
            ResponseEntity<MemberDto> receiveMember = circuitBreaker.run(() -> plantServiceClient.findById(memberNo.longValue()),
                    throwable -> ResponseEntity.ok(null));
            String content = message.getContentType().equals("image")
                                    ? "image" : message.getContent();
            // 알람을 보낼 URL을 생성한다.
            String sendUrl = getNotificationUrl(message.getTradeBoardNo(), message.getChatNo(), memberDto.getBody().getNickname());

            //알림 전송
            notificationService.send(memberDto.getBody().getId().intValue(), receiveMember.getBody().getId().intValue(), NotifiTypeEnum.CHAT, sendUrl, content);
        }
        // 보낸 사람일 경우에만 메시지를 저장 -> 중복 저장 방지
        if (message.getSenderEmail().equals(memberDto.getBody().getEmail())) {
            // Message 객체를 채팅 엔티티로 변환
            Chatting chatting = message.convertEntity();
            // 채팅 내용을 저장
            Chatting savedChat = mongoChatRepository.save(chatting);
            // 저장된 고유 ID를 반환
            message.setId(savedChat.getId());
        }
        return message;
    }
    /**
     * 참가자 입장 알림 메서드
     * @param : String email, Integer chatRoomNo
     */
    public void updateMessage(String email, Integer chatRoomNo) {
        Message message = Message.builder()
                .contentType("notice")
                .chatNo(chatRoomNo)
                .content(email + " 님이 돌아오셨습니다.")
                .build();

        sender.send(KafkaUtil.KAFKA_TOPIC, message);
    }
    /**
     * 읽지 않은 메시지 채팅장 입장시 읽음 처리 메서드
     * @param : Integer chatNo, String username
     */
    public void updateCountAllZero(Integer chatNo, String username) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        ResponseEntity<MemberDto> findMember = circuitBreaker.run(() -> plantServiceClient.findByUsername(username),
                throwable -> ResponseEntity.ok(null));
        //MongoDb Update Query
        Update update = new Update().set("readCount", 0);
        //ne-> not equal
        Query query = new Query(where("chatRoomNo").is(chatNo)
                .and("senderNo").ne(findMember.getBody().getId().intValue()));

        mongoTemplate.updateMulti(query, update, Chatting.class);
    }
    /**
     * 읽지 않은 메시지 카운트 메서드
     * @param : Integer chatNo, Integer senderNo
     */
    long countUnReadMessage(Integer chatRoomNo, Integer senderNo) {
        Query query = new Query(where("chatRoomNo").is(chatRoomNo)
                .and("readCount").is(1)
                .and("senderNo").ne(senderNo));

        return mongoTemplate.count(query, Chatting.class);
    }

    private String getNotificationUrl(Integer tradeBoardNo, Integer chatNo, String nickname) {
        return chatNo +
                "/" +
                tradeBoardNo+
        "/" +nickname;
    }


    /**
     * 판매자가 참가한  채팅방이 존재하는지 유무 처리 메서드
     * 단순 조회용 메서드라 readOnly = true
     * @param : Integer tradeBoardNo,  Integer memberNo
     */
    @Transactional(readOnly = true)
    public Boolean existChatRoomBySeller(Integer tradeBoardNo, Integer memberNo) {
        return chatRepository.existChatRoomBySeller(tradeBoardNo, memberNo);
    }

    /**
     * 채팅방 삭제 메서드
     * plant-service에서 kafka를 통해 거래 게시글 삭제 요청을 받으면 삭제
     *
     * @param : Integer tradeBoardNo
     */
    @Transactional
    public void deleteChatRoom(Integer tradeBoardNo) {
        List<Integer> chatRoomNoList= chatRoomService.delete(tradeBoardNo);

        deleteChatting(chatRoomNoList);

    }
    @Transactional
    public void deleteChatting(List<Integer> chatRoomNoList) {
        // Set 자료구조를 활용하여 중복된 chatNo 제거
        Set<Integer> uniqueChatNoSet = new HashSet<>(chatRoomNoList);

        // 중복 제거 후의 chatNo에 해당하는 채팅 데이터 삭제
        mongoTemplate.remove(query(where("chatRoomNo").in(uniqueChatNoSet)), Chatting.class);
    }
}
