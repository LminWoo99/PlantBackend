package Plant.PlantProject.dto.response;

import Plant.PlantProject.domain.Entity.TradeBoard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeInfoResponseDto {
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
    private String keywordContent;

    public static TradeInfoResponseDto convertTradeBoardToDto(TradeBoard tradeBoard) {
        return new TradeInfoResponseDto(tradeBoard.getId(), tradeBoard.getTitle(),tradeBoard.getContent(),tradeBoard.getCreateBy(),
                tradeBoard.getMember().getId(),  tradeBoard.getView(),tradeBoard.getStatus().name(),
                tradeBoard.getCreatedAt(), tradeBoard.getUpdatedAt(), tradeBoard.getPrice(),
                tradeBoard.getGoodCount(), tradeBoard.getBuyer(), tradeBoard.getKeywordContent());
    }

}
