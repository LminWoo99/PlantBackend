package Plant.PlantProject.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsRequestDto {
    private Long id;
    private Long memberId;
    private Long tradeBoardId;

}
