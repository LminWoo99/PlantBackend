package Plant.PlantProject.repository;

import Plant.PlantProject.Entity.Comment;

import java.util.List;

public interface CustomCommentRepository {
    //findCommentsByTicketIdWithParentOrderByParentIdAscNullsFirstCreatedAtAsc
    List<Comment> findCommentByTradeBoardId(Long tradeBoardId);
}