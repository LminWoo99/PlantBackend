package com.example.plantchatservice.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
/**
 * mysql에 저장할 notification entity
 * 채팅 알림 도메인
 * 엔티티에는 @NoArgsConstructor(access = AccessLevel.PROTECTED)
 * @AllArgsConstructor , @Setter지양
 * @DynamicInsert로 수정되는 컬럼만 쿼리
 */
@Entity
@Getter
@Table(name = "NOTIFICATION")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity{
    @Id
    @Column(name = "notifi_No")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notifiNo;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type")
    private NotifiTypeEnum typeEnum;

    @Column(name = "url")
    private String url;

    @Column(name = "content")
    private String content;

    @Column(name = "delete_status")
    private boolean isDel;

    @Column(name = "read_status")
    private boolean isRead;

    @Column(name = "sender_No")
    private Integer senderNo;

    @Column(name = "receiver_No")
    private Integer receiverNo;

    @Column(name = "sender_Name")
    private String senderName;

    @Builder
    public Notification(Long notifiNo, NotifiTypeEnum typeEnum, String url, String content, boolean isDel, boolean isRead, Integer senderNo, Integer receiverNo) {
        this.notifiNo = notifiNo;
        this.typeEnum = typeEnum;
        this.url = url;
        this.content = content;
        this.isDel = isDel;
        this.isRead = isRead;
        this.senderNo = senderNo;
        this.receiverNo = receiverNo;
    }
    public void read() {
        this.isRead = true;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
