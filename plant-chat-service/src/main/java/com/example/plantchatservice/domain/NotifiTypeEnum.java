package com.example.plantchatservice.domain;

import lombok.Getter;

@Getter
public enum NotifiTypeEnum {
    NOTICE("notice/","announcement"),
    CHAT("chat/","chat");

    private final String path;
    private final String alias;

    NotifiTypeEnum(String path, String alias) {
        this.path = path;
        this.alias = alias;
    }
}
