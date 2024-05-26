package Plant.PlantProject.dto.response;

import Plant.PlantProject.domain.Entity.Status;
import Plant.PlantProject.domain.Entity.TradeBoard;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeBoardResponseDto{
    private Long id;
    private String title;
    private String content;
    private String createBy;
    private Long memberId;
    private Integer view;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer price;
    private Integer goodCount;
    private String buyer;
    private String keywordContent;
    private List<String> imageUrls;

    public TradeBoardResponseDto(Long id, String title, String content, String createBy, Long memberId, Integer view, Status status, LocalDateTime createdAt, LocalDateTime updatedAt, Integer price, Integer goodCount, String buyer, String keywordContent, List<String> imageUrls) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createBy = createBy;
        this.memberId = memberId;
        this.view = view;
        this.status = status.name();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.price = price;
        this.goodCount = goodCount;
        this.buyer = buyer;
        this.keywordContent = keywordContent;
        this.imageUrls = imageUrls;
    }

    public static TradeBoardResponseDto convertTradeBoardToDto(TradeBoard tradeBoard) {

        List<String> imageUrls = tradeBoard.getImageList().stream()
                .map(image -> image.getUrl()).collect(Collectors.toList());

        return new TradeBoardResponseDto(tradeBoard.getId(), tradeBoard.getTitle(),tradeBoard.getContent(),tradeBoard.getCreateBy(),
                tradeBoard.getMember().getId(),  tradeBoard.getView(),tradeBoard.getStatus().name(),
                tradeBoard.getCreatedAt(), tradeBoard.getUpdatedAt(), tradeBoard.getPrice(),
                tradeBoard.getGoodCount(), tradeBoard.getBuyer(), tradeBoard.getKeywordContent(), imageUrls
        );
    }

}
