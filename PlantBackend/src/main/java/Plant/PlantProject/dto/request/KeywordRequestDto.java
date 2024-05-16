package Plant.PlantProject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeywordRequestDto {
    private String keywordContent;
    private Integer memberNo;


}
