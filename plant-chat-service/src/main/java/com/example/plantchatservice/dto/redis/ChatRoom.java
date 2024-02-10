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
    private Long chatroomNo;

    @Indexed
    private String nickname;

    @Builder
    public ChatRoom(Long chatroomNo, String nickname) {
        this.chatroomNo = chatroomNo;
        this.nickname = nickname;
    }
}
