package Plant.PlantProject.dto;

import Plant.PlantProject.Entity.Status;
import Plant.PlantProject.Entity.TradeBoard;
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

    private String tradeTitle;

    private String tradeContent;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Builder
    public TradeBoardDto(Long id, String tradeTitle, String tradeContent, Status status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.tradeTitle = tradeTitle;
        this.tradeContent = tradeContent;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    @Builder
    public TradeBoardDto(String tradeTitle, String tradeContent){
        this.tradeTitle = tradeTitle;
        this.tradeContent = tradeContent;
    }

    public TradeBoardDto(TradeBoard tradeBoard) {
        this.id = tradeBoard.getId();
        this.tradeTitle = tradeBoard.getTradeTitle();
        this.tradeContent = tradeBoard.getTradeContent();
        this.status = tradeBoard.getStatus();
        this.createdAt = tradeBoard.getCreatedAt();
        this.updatedAt = tradeBoard.getUpdatedAt();

    }


    public TradeBoard toEntity() {
        return TradeBoard.builder()
                .id(id)
                .tradeTitle(tradeTitle)
                .tradeContent(tradeContent)
                .status(status)
                .build();
    }
}
