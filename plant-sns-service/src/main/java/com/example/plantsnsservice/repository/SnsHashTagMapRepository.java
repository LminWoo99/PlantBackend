package com.example.plantsnsservice.repository;

import com.example.plantsnsservice.domain.entity.SnsHashTagMap;
import com.example.plantsnsservice.domain.entity.SnsPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SnsHashTagMapRepository extends JpaRepository<SnsHashTagMap, Long> {
    List<SnsHashTagMap> findAllBySnsPost(SnsPost snsPost);
}
