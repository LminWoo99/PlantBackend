package Plant.PlantProject.repository;

import Plant.PlantProject.Entity.Comment;

import java.util.List;

public interface CustomCommentRepository {

    List<Comment> findCommentByTradeBoardId(Long tradeBoardId);
    void updateComment(Comment comment);
}