package Plant.PlantProject.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@ToString
public class ChatRoomResponseDto {

    private Integer chatNo;

    private Integer createMember;

    private Integer joinMember;

    private Integer tradeBoardNo;

    private long regDate;
    private Participant participant;
    private LatestMessage latestMessage;

    private long unReadCount;

    public void setUnReadCount(long unReadCount) {
        this.unReadCount = unReadCount;
    }

    public ChatRoomResponseDto(Integer chatNo, Integer createMember, Integer joinMember, Integer tradeBoardNo, LocalDateTime regDate
                              ) {
        this.chatNo = chatNo;
        this.createMember = createMember;
        this.joinMember = joinMember;
        this.tradeBoardNo = tradeBoardNo;
        this.regDate = regDate.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
//        this.participant = participant;
//        this.latestMessage = latestMessage;
//        this.unReadCount = unReadCount;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public void setLatestMessage(LatestMessage latestMessage) {
        this.latestMessage = latestMessage;
    }

    @Getter
    @AllArgsConstructor
    @ToString
    public static class Participant {
        private String username;
        private String nickname;
    }

    @Getter
    @ToString
    public static class LatestMessage {
        private String context;
        private long sendAt;

        @Builder
        public LatestMessage(String context, LocalDateTime sendAt) {
            this.context = context;
            this.sendAt = sendAt.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        }
    }
}
