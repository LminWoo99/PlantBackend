package Plant.PlantProject.repository;

import Plant.PlantProject.domain.Entity.Comment;

import java.util.List;

public interface CustomCommentRepository {

    List<Comment> findCommentByTradeBoardId(Long tradeBoardId);
    void updateComment(Comment comment);
}