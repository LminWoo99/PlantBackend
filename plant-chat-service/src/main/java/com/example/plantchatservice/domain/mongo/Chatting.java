package com.example.plantchatservice.domain.mongo;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Document(collection="chatting")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//MongoDB 채팅 모델
public class Chatting {
    @Id
    private String id;
    private Integer chatRoomNo;
    private Long senderNo;
    private String senderName;
    private String contentType;
    private String content;
    private LocalDateTime sendDate;
    private long readCount;
    /**
     * class에 @AllArgsConstructor 붙이지 말고
     * 생성자 선언해서 @Builder
     */
    @Builder
    public Chatting(String id, Integer chatRoomNo, Long senderNo, String senderName, String contentType, String content, LocalDateTime sendDate, long readCount) {
        this.id = id;
        this.chatRoomNo = chatRoomNo;
        this.senderNo = senderNo;
        this.senderName = senderName;
        this.contentType = contentType;
        this.content = content;
        this.sendDate = sendDate;
        this.readCount = readCount;
    }
}
