package com.example.plantsnsservice.repository;

import com.example.plantsnsservice.domain.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashTagRepository extends JpaRepository<HashTag , Long> {
}
