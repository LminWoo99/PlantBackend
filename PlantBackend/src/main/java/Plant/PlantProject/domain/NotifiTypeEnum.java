package Plant.PlantProject.domain;

import lombok.Getter;

@Getter
public enum NotifiTypeEnum {
    SNS_COMMENT("snspostlist/","comment"),
    TRADEBOARD_GOODS("bbsdetail/","goods"),
    SNS_HEART("snspostlist/","reply"),
    CHAT("chatroom/","chat");

    private final String path;
    private final String alias;

    NotifiTypeEnum(String path, String alias) {
        this.path = path;
        this.alias = alias;
    }
}
