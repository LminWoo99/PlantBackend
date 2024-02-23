package com.example.plantchatservice.dto.chat;

import com.example.plantchatservice.domain.mongo.Chatting;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    private String id;
    @NotNull
    private Integer chatNo;
    @NotNull
    private String contentType;
    @NotNull
    private String content;
    private String senderName;
    private Integer senderNo;
    @NotNull
    private Integer tradeBoardNo;

    private long sendTime;
    private Integer readCount;
    private String senderEmail;


    public void setSendTimeAndSender(LocalDateTime sendTime, Integer senderNo, String senderName, Integer readCount) {
        this.senderName = senderName;
        this.sendTime = sendTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
//        this.sendTime=sendTime.toInstant(ZoneOffset.UTC).toEpochMilli();
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
//    @Builder
//    public Message(String id, @NotNull Integer chatNo, @NotNull String contentType, @NotNull String content, String senderName, Long senderNo, @NotNull Long tradeBoardNo, long sendTime, Integer readCount, String senderEmail) {
//        this.id = id;
//        this.chatNo = chatNo;
//        this.contentType = contentType;
//        this.content = content;
//        this.senderName = senderName;
//        this.senderNo = senderNo;
//        this.tradeBoardNo = tradeBoardNo;
//        this.sendTime = sendTime;
//        this.readCount = readCount;
//        this.senderEmail = senderEmail;
//    }
}
