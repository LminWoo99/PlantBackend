package Plant.PlantProject.domain.dto;

import Plant.PlantProject.domain.Entity.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDto {
    private Long id;
    private Long memberId;
    private Long tradeBoardId;

    public static GoodsDto convertGoodsToDto(Goods goods) {

        return new GoodsDto(goods.getId(), goods.getMember().getId(), goods.getTradeBoard().getId());

    }
}
