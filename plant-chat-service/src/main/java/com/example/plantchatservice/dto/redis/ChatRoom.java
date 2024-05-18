package com.example.plantchatservice.dto.redis;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@RedisHash(value = "chatRoom")
public class ChatRoom {
    @Id
    private String id;

    @Indexed
    private Integer chatroomNo;

    @Indexed
    private Integer tradeBoardNo;

    @Indexed
    private String username;

    @Builder
    public ChatRoom(Integer chatroomNo, Integer tradeBoardNo, String username) {
        this.chatroomNo = chatroomNo;
        this.tradeBoardNo = tradeBoardNo;
        this.username = username;
    }
}
