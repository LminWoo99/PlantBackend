package Plant.PlantProject.dto;

import Plant.PlantProject.Entity.Status;
import Plant.PlantProject.Entity.TradeBoard;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
    private String tTitle;
    private String tContent;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Builder
    public TradeBoardDto(Long id, String tTitle, String tContent, Status status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.tTitle = tTitle;
        this.tContent = tContent;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public TradeBoardDto(String tTitle, String tContent){
        this.tTitle = tTitle;
        this.tContent = tContent;
    }

    public TradeBoard toEntity() {
        return TradeBoard.builder()
                .id(id)
                .tTitle(tTitle)
                .tContent(tContent)
                .status(status)
                .build();
    }
}
