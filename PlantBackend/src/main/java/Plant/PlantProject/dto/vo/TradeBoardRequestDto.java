package Plant.PlantProject.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeBoardRequestDto {
    private Long id;
    private String title;
    private String content;
    private Long memberId;
    private String createBy;
    private int view;
    private int price;
    private int goodCount;
    private String buyer;

}
