package Plant.PlantProject.dto;

import Plant.PlantProject.Entity.Member;
import Plant.PlantProject.Entity.Status;
import Plant.PlantProject.Entity.TradeBoard;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/*
 작성자 : 이민우
 작성 일자: 02.19
 내용 : 거래 게시글 DTO
 특이 사항: setter사용을 지양하고, builder 패턴 지향
*/
@Data
@NoArgsConstructor
public class TradeBoardDto {
    private Long id;
    @JsonIgnore
    private Member member;
    private String createBy;
    private String title;

    private String content;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Builder
    public TradeBoardDto(Long id, String createBy, Member member, String title, String content, Status status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.createBy = createBy;
        this.member = member;
        this.title = title;
        this.content = content;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public TradeBoardDto(Long id, String title, String content){
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public TradeBoardDto(TradeBoard tradeBoard) {
        this.id = tradeBoard.getId();
        this.member = tradeBoard.getMember();
        this.createBy = tradeBoard.getCreateBy();
        this.title = tradeBoard.getTitle();
        this.content = tradeBoard.getContent();
        this.status = tradeBoard.getStatus();
        this.createdAt = tradeBoard.getCreatedAt();
        this.updatedAt = tradeBoard.getUpdatedAt();

    }


    public TradeBoard toEntity() {
        return TradeBoard.builder()
                .member(member)
                .createBy(createBy)
                .id(id)
                .title(title)
                .content(content)
                .status(status)
                .build();
    }
}
