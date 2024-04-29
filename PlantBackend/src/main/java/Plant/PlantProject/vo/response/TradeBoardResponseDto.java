package Plant.PlantProject.vo.response;

import Plant.PlantProject.domain.Entity.TradeBoard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeBoardResponseDto {
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
    private List<String> imageUrls;

    public static TradeBoardResponseDto convertTradeBoardToDto(TradeBoard tradeBoard) {
        List<String> imageList = tradeBoard.getImageList().stream().map(image -> {
            String url = image.getUrl();
            return url;
        }).collect(Collectors.toList());
        return new TradeBoardResponseDto(tradeBoard.getId(), tradeBoard.getTitle(),tradeBoard.getContent(),tradeBoard.getCreateBy(),
                tradeBoard.getMember().getId(),  tradeBoard.getView(),tradeBoard.getStatus().name(),
                tradeBoard.getCreatedAt(), tradeBoard.getUpdatedAt(), tradeBoard.getPrice(),
                tradeBoard.getGoodCount(), tradeBoard.getBuyer(), imageList
        );
    }

}
