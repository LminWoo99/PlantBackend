package Plant.PlantProject.vo.response;

import Plant.PlantProject.domain.Entity.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsResponseDto {
    private Long id;
    private Long memberId;
    private Long tradeBoardId;

    public static GoodsResponseDto convertGoodsToDto(Goods goods) {

        return new GoodsResponseDto(goods.getId(), goods.getMember().getId(), goods.getTradeBoard().getId());

    }
}
