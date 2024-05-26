package Plant.PlantProject.dto.request;

import Plant.PlantProject.domain.Entity.Member;
import Plant.PlantProject.domain.Entity.Status;
import Plant.PlantProject.domain.Entity.TradeBoard;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeBoardRequestDto {
    private Long id;

    private Long memberNo;
    private String title;

    private String content;
    private Status status;
    private int price;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;
    private int view;
    private Integer goodCount;

    private String username;
    private String buyer;

    private String keyWordContent;

    public TradeBoard toEntity(String createBy, Member member, String title, String content, Integer price, String keywordContent){
        TradeBoard tradeBoard=TradeBoard.builder()
                .member(member)
                .title(title)
                .content(content)
                .createBy(createBy)
                .price(price)
                .keywordContent(keywordContent)
                .build();
        return tradeBoard;
    }

}
