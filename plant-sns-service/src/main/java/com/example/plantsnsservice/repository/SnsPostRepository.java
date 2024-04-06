package com.example.plantsnsservice.repository;

import com.example.plantsnsservice.domain.SnsPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SnsPostRepository extends JpaRepository<SnsPost, Long> {
    List<SnsPost> findAllByOrderByCreatedAtDesc();
}
