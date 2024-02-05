package Plant.PlantProject.dto;

import Plant.PlantProject.Entity.Member;
import Plant.PlantProject.Entity.Status;
import Plant.PlantProject.Entity.TradeBoard;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private int price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int view;
    @Builder
    public TradeBoardDto(Long id, String createBy, Member member, String title, String content, Status status, LocalDateTime createdAt, LocalDateTime updatedAt, int view) {
        this.id = id;
        this.createBy = createBy;
        this.member = member;
        this.title = title;
        this.content = content;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.view=view;
    }
    public TradeBoardDto(Long id, String title, String content){
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public TradeBoard toEntity() {
        return TradeBoard.builder()
                .member(member)
                .createBy(createBy)
                .id(id)
                .title(title)
                .content(content)
                .status(status)
                .view(view)
                .build();
    }
}
