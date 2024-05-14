package com.example.plantchatservice.domain;

import lombok.Getter;

@Getter
public enum NotifiTypeEnum {
    SNS_COMMENT("snspostlist/","댓글"),
    TRADEBOARD_GOODS("bbsdetail/","찜"),
    SNS_HEART("snspostlist/","좋아요"),
    CHAT("chatroom/","채팅");

    private final String path;
    private final String alias;

    NotifiTypeEnum(String path, String alias) {
        this.path = path;
        this.alias = alias;
    }
}
