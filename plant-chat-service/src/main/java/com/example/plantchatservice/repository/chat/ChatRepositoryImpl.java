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
    //채팅방 해당 거래 게시글, 거래 게시글 올린 사용자와 만든 채팅방이 있는지 확인
    //존재 ==> true, 존재 x==> false
    public boolean existChatRoomByBuyer(Integer tradeBoardNo, Integer createMemberNo, Integer joinMemberNo) {
        BooleanExpression conditions = chat.tradeBoardNo.eq(tradeBoardNo)
                .and(chat.createMember.eq(createMemberNo))
                .and(chat.joinMember.eq(joinMemberNo));

        return !jpaQueryFactory.selectFrom(chat)
                .where(conditions)
                .fetch()
                .isEmpty();
    }
    //거래 게시글 올린 사람이 참가하거나 만든 채팅방 존재하는지 확인
    //존재 ==> true, 존재 x==> false
    public boolean existChatRoomBySeller(Integer tradeBoardNo, Integer tradeMemberNo) {
        BooleanExpression conditions = chat.tradeBoardNo.eq(tradeBoardNo)
                .and(chat.joinMember.eq(tradeMemberNo));

        return !jpaQueryFactory.selectFrom(chat)
                .where(conditions)
                .fetch()
                .isEmpty();
    }


    public List<Integer> deleteChatRoomAndReturnChatNo(Integer tradeBoardNo) {
        // 삭제 전에 삭제할 데이터의 chatNo를 조회하여 가져옴
        List<Integer> deletedChatNos = jpaQueryFactory.select(chat.chatNo)
                .from(chat)
                .where(chat.tradeBoardNo.eq(tradeBoardNo))
                .fetch();

        // 실제 삭제 작업 실행
        jpaQueryFactory.delete(chat)
                .where(chat.tradeBoardNo.eq(tradeBoardNo))
                .execute();

        // 삭제된 chatNo 목록 반환
        return deletedChatNos;
    }
    public Chat findByTradeBoardNoAndChatNo(Integer tradeBoardNo, Integer createMemberNo) {
        return jpaQueryFactory.selectFrom(chat)
                .where(chat.tradeBoardNo.eq(tradeBoardNo).and(chat.createMember.eq(createMemberNo)))
                .fetchOne();

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
                .fetch();
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
