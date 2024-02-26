package com.example.plantchatservice.repository.chat;

import com.example.plantchatservice.domain.Chat;
import com.example.plantchatservice.dto.vo.ChatRoomResponseDto;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomChatRepository {

    List<Chat> findChattingRoom(@Param("memberNo") Integer memberNo);

    List<ChatRoomResponseDto> getChattingList(Integer memberNo, Integer tradeBoardNo);

    Chat getChatting(Integer chatNo, Integer senderNo);

    Integer getReceiverMember(Integer chatNo, Integer senderNo);

    boolean existChatRoomByBuyer(Integer tradeBoardNo, Integer createMemberNo, Integer joinMemberNo);

    Chat findByTradeBoardNoAndChatNo(Integer tradeBoardNo, Integer joinMemberNo);

    boolean existChatRoomBySeller(Integer tradeBoardNo, Integer tradeMemberNo);





}
