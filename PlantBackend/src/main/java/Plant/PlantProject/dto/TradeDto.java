package Plant.PlantProject.dto;

import Plant.PlantProject.Entity.Comment;
import Plant.PlantProject.Entity.DeleteStatus;
import Plant.PlantProject.Entity.TradeBoard;
import Plant.PlantProject.repository.GoodsRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeDto {
    private Long id;
    private String title;
    private String content;
    private String createBy;
    private Long memberId;
    private int view;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int price;
    private int goodCount;
    private String buyer;
    public static TradeDto convertTradeBoardToDto(TradeBoard tradeBoard) {
        return new TradeDto(tradeBoard.getId(), tradeBoard.getTitle(),tradeBoard.getContent(),tradeBoard.getCreateBy(),
                tradeBoard.getMember().getId(),  tradeBoard.getView(),tradeBoard.getStatus().name(),
                tradeBoard.getCreatedAt(), tradeBoard.getUpdatedAt(), tradeBoard.getPrice(),
                tradeBoard.getGoodCount(), tradeBoard.getBuyer()
        );

    }

}
