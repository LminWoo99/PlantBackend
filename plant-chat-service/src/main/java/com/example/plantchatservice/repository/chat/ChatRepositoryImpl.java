package com.example.plantchatservice.repository.chat;

import com.example.plantchatservice.domain.Chat;
import com.example.plantchatservice.domain.QChat;
import com.example.plantchatservice.dto.vo.ChatRoomResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Objects;

import static com.example.plantchatservice.domain.QChat.chat;

@RequiredArgsConstructor
public class ChatRepositoryImpl implements CustomChatRepository {
    private final JPAQueryFactory jpaQueryFactory;

    // 내가 만든 채팅방또는 내가 참여중인 채팅방을 전부 찾아주기
    public List<Chat> findChattingRoom(@Param("memberNo") Integer memberNo) {
        return jpaQueryFactory.selectFrom(chat)
                .where(chat.chatNo.eq(memberNo)
                        .or(chat.joinMember.eq(memberNo)))
                .fetch();
    }

    // 채팅방 리스트 조회
    //엔티티와 다른 반환 타입인 경우 Projections를 사용
    public List<ChatRoomResponseDto> getChattingList(Integer memberNo, Integer tradeBoardNo) {
        return jpaQueryFactory.select(Projections.constructor(ChatRoomResponseDto.class,
                        chat.chatNo,
                        chat.createMember,
                        chat.joinMember,
                        chat.tradeBoardNo,
                        chat.regDate))
                .from(chat)
                .where(chat.createMember.eq(memberNo).or(chat.joinMember.eq(memberNo)), tradeBoardNoEq(tradeBoardNo))
                .fetch()
                ;
    }
    // 채팅방 한건 조회
    public Chat getChatting(Integer chatNo, Integer senderNo) {
        Chat chatRoom = jpaQueryFactory.select(QChat.chat)
                .from(QChat.chat)
                .where(QChat.chat.chatNo.eq(chatNo))
                .fetchOne();
        return chatRoom;
    }

    private BooleanExpression tradeBoardNoEq(Integer tradeBoardNo) {
        return Objects.nonNull(tradeBoardNo) ? chat.tradeBoardNo.eq(tradeBoardNo) : null;
    }
    // 현재 메시지를 받는 사람을 조회하는 메서드
    public Integer getReceiverMember(Integer chatNo, Integer senderNo) {
        Chat chatRoom = jpaQueryFactory.select(chat)
                .from(chat)
                .where(chat.chatNo.eq(chatNo))
                .fetchOne();
        Integer memberNo = chatRoom.getCreateMember().equals(senderNo) ?
                chatRoom.getJoinMember() : chatRoom.getCreateMember();

        return memberNo;
    }

}
