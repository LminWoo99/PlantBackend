package Plant.PlantProject.dto;

import Plant.PlantProject.Entity.Comment;
import Plant.PlantProject.Entity.DeleteStatus;
import Plant.PlantProject.Entity.TradeBoard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static Plant.PlantProject.Entity.DeleteStatus.Y;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto implements Serializable {

    private Long id;
    private String content;
    private Long tradeBoardId;
    private Long memberId;
    private String nickname;
    private List<CommentDto> children = new ArrayList<>();
    private String isDeleted;
    private String secret;

    public CommentDto(Long id, String content, Long tradeBoardId,Long memberId, String nickname, String isDeleted,String secret) {
        this.id = id;
        this.content = content;
        this.tradeBoardId = tradeBoardId;
        this.memberId = memberId;
        this.nickname = nickname;
        this.isDeleted = isDeleted;
        this.secret = secret;
    }

    public static CommentDto convertCommentToDto(Comment comment) {
        return comment.getIsDeleted() == Y ?
                new CommentDto(comment.getId(), "삭제된 댓글입니다.", null, null,null, Y.name(), null) :
                new CommentDto(comment.getId(), comment.getContent(), comment.getTradeBoardId().getId(), comment.getMember().getId(),comment.getMember().getNickname(), comment.getIsDeleted().name(),
                        comment.getSecret());
    }

}