package com.example.plantchatservice.repository;

import com.example.plantchatservice.domain.Chat;
import com.example.plantchatservice.dto.vo.ChatRoomResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CustomChatRepository {

    List<Chat> findChattingRoom(@Param("memberNo") Integer memberNo);

    List<ChatRoomResponseDto> getChattingList(Integer memberNo, Integer tradeBoardNo);

    Chat getChatting(Integer chatNo, Integer senderNo);




}
