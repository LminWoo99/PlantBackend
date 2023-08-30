package Plant.PlantProject.repository;

import Plant.PlantProject.Entity.Comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
}