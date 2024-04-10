package com.example.plantsnsservice.repository.querydsl;

import com.example.plantsnsservice.domain.entity.SnsComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnsCommentRepository extends JpaRepository<SnsComment, Long> , CustomSnsCommentRepository {
}
