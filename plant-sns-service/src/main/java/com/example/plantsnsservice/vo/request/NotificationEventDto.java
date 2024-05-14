package com.example.plantsnsservice.vo.request;

import com.example.plantsnsservice.domain.NotifiTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationEventDto implements Serializable {
    private Integer senderNo;
    private Integer receiverNo;
    private NotifiTypeEnum type;
    private String resource;
    private String content;

}
