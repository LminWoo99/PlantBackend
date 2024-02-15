package com.example.plantchatservice.dto.notification;

import com.example.plantchatservice.domain.NotifiTypeEnum;
import com.example.plantchatservice.domain.Notification;
import com.example.plantchatservice.common.util.LocalDateTimeUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NotificationResponse {
    private Long id;
    private String type;
    private String content;
    private String url;
    private Integer[] publishedAt;
    private Integer senderNo;

    @JsonProperty("checked")
    private boolean read;
    @JsonProperty("del")
    private boolean del;

    @Builder
    public NotificationResponse(Long id, NotifiTypeEnum type, String content, String url, LocalDateTime publishedAt, Integer senderNo, boolean read, boolean del) {
        this.id = id;
        this.type = type.getAlias();
        this.content = content;
        this.url = url;
        this.publishedAt = LocalDateTimeUtils.toArray(publishedAt);
        this.senderNo = senderNo;
        this.read = read;
        this.del = del;
    }

    public static NotificationResponse toDto(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getNotifiNo())
                .type(notification.getTypeEnum())
                .content(notification.getContent())
                .url(notification.getUrl())
                .publishedAt(notification.getRegDate())
                .read(notification.isRead())
                .del(notification.isDel())
                .senderNo(notification.getSenderNo())
                .build();

    }
}
