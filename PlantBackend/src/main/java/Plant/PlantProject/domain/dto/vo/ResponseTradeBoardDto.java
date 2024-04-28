package Plant.PlantProject.domain.dto.vo;

import Plant.PlantProject.domain.Entity.TradeBoard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTradeBoardDto {
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
    public static ResponseTradeBoardDto convertTradeBoardToDto(TradeBoard tradeBoard) {
        return new ResponseTradeBoardDto(tradeBoard.getId(), tradeBoard.getTitle(),tradeBoard.getContent(),tradeBoard.getCreateBy(),
                tradeBoard.getMember().getId(),  tradeBoard.getView(),tradeBoard.getStatus().name(),
                tradeBoard.getCreatedAt(), tradeBoard.getUpdatedAt(), tradeBoard.getPrice(),
                tradeBoard.getGoodCount(), tradeBoard.getBuyer()
        );

    }

}
