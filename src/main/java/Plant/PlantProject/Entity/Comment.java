package Plant.PlantProject.Entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

/*
 작성자 : 이민우
 작성 일자: 02.18
 내용 : 댓글 엔티티
 특이 사항: 없음
*/
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment{
    @Id
    @GeneratedValue   //jpa 어노테이션인데 그냥 기본키 어노테이션으로 알고있으면됨
    @Column(name = "CommentId")
    private Long id;  //고유번호
    private String commentContent;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private InfoBoard infoBoard;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Member member;
}
