package Plant.PlantProject.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequestDto {
    private String content;
    private Long tradeBoardId;
    private Long memberId;
    private Long parentId;
    private String isDeleted;
    private String secret;
}