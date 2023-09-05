package Plant.PlantProject.Entity;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "comment") // 테이블 이름 지정 (DB 테이블 이름과 일치해야 함)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id; // 댓글 고유 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 댓글 작성자
    @Column(nullable = false)
    @Lob
    private String content; // 댓글 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tradeBoard_id")
    private TradeBoard tradeBoardId;
    // 게시글 번호


    @Enumerated(value = EnumType.STRING)
    private DeleteStatus isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    private String secret;

    public static Comment createComment(Member member, String content, TradeBoard tradeBoardId, Comment parent, String secret) {
        Comment comment = new Comment();
        comment.member = member;
        comment.content = content;
        comment.tradeBoardId = tradeBoardId;
        comment.parent = parent;
        comment.isDeleted = DeleteStatus.N;
        comment.secret = secret;
        return comment;
    }
    public void changeDeletedStatus(DeleteStatus deleteStatus) {
        this.isDeleted = deleteStatus;
    }

    public void setContent(String content) {
        this.content = content;
    }
}