package Plant.PlantProject.vo.response;

import Plant.PlantProject.domain.Entity.TradeBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponseDto {
    private String name;
    private String url;
    private TradeBoard tradeBoard;


}
