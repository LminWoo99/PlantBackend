package com.example.plantchatservice.dto.chat;

import com.example.plantchatservice.domain.mongo.Chatting;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message implements Serializable {
    private String id;
    @NotNull
    private Long chatNo;
    @NotNull
    private String contentType;
    @NotNull
    private String content;
    private String senderName;
    private Long senderNo;
    @NotNull
    private Long tradeBoardNo;
    private long sendTime;
    private Integer readCount;
    private String senderEmail;

    public void setSendTimeAndSender(LocalDateTime sendTime, Long senderNo, String senderName, Integer readCount) {
        this.senderName = senderName;
        this.sendTime = sendTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        this.senderNo = senderNo;
        this.readCount = readCount;
    }

    public void setId(String id) {
        this.id = id;
    }
    public Chatting convertEntity() {
        return Chatting.builder()
                .senderName(senderName)
                .senderNo(senderNo)
                .chatRoomNo(chatNo)
                .contentType(contentType)
                .content(content)
                .sendDate(Instant.ofEpochMilli(sendTime).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime())
                .readCount(readCount)
                .build();
    }
    @Builder
    public Message(String id, @NotNull Long chatNo, @NotNull String contentType, @NotNull String content, String senderName, Long senderNo, @NotNull Long tradeBoardNo, long sendTime, Integer readCount, String senderEmail) {
        this.id = id;
        this.chatNo = chatNo;
        this.contentType = contentType;
        this.content = content;
        this.senderName = senderName;
        this.senderNo = senderNo;
        this.tradeBoardNo = tradeBoardNo;
        this.sendTime = sendTime;
        this.readCount = readCount;
        this.senderEmail = senderEmail;
    }
}
