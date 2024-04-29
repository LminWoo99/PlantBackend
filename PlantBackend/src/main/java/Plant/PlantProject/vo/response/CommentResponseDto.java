package Plant.PlantProject.vo.response;

import Plant.PlantProject.domain.Entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static Plant.PlantProject.domain.Entity.DeleteStatus.Y;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto implements Serializable {

    private Long id;
    private String content;
    private Long tradeBoardId;
    private Long memberId;
    private String nickname;
    private List<CommentResponseDto> children = new ArrayList<>();
    private String isDeleted;
    private String secret;

    public CommentResponseDto(Long id, String content, Long tradeBoardId, Long memberId, String nickname, String isDeleted, String secret) {
        this.id = id;
        this.content = content;
        this.tradeBoardId = tradeBoardId;
        this.memberId = memberId;
        this.nickname = nickname;
        this.isDeleted = isDeleted;
        this.secret = secret;
    }

    public static CommentResponseDto convertCommentToDto(Comment comment) {
        return comment.getIsDeleted() == Y ?
                new CommentResponseDto(comment.getId(), "삭제된 댓글입니다.", null, null,null, Y.name(), null) :
                new CommentResponseDto(comment.getId(), comment.getContent(), comment.getTradeBoardId().getId(), comment.getMember().getId(),comment.getMember().getNickname(), comment.getIsDeleted().name(),
                        comment.getSecret());
    }

}