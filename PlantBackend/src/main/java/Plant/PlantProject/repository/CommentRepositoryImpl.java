package Plant.PlantProject.repository;

import Plant.PlantProject.Entity.Comment;

import Plant.PlantProject.Entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static Plant.PlantProject.Entity.QComment.*;
import static Plant.PlantProject.Entity.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory queryFactory;
    @Override
    public List<Comment> findCommentByTradeBoardId(Long tradeBoardId) {
        return queryFactory.selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(comment.tradeBoardId.id.eq(tradeBoardId))
                .orderBy(
                        comment.parent.id.asc().nullsFirst()
                ).fetch();
    }
    @Override
    public void updateComment(Comment comment) {
        queryFactory.update(QComment.comment)
                .where(QComment.comment.id.eq(comment.getId())) // 댓글 ID로 조건 설정
                .set(QComment.comment.content, comment.getContent()) // 댓글 내용 업데이트
                .execute();
    }
}