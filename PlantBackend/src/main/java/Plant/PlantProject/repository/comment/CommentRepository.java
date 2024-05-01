package Plant.PlantProject.repository.comment;

import Plant.PlantProject.domain.Entity.Comment;
import Plant.PlantProject.domain.Entity.TradeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {


    @Query("select c from Comment c left join fetch c.parent where c.id = :id")
    Comment findCommentByIdWithParent(@Param("id") Long id);

    Page<Comment> findCommentByTradeBoardId(TradeBoard tradeBoard, Pageable pageable);


    void deleteAllByTradeBoardId(TradeBoard tradeBoard);
}