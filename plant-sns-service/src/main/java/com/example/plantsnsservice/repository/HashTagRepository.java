package com.example.plantsnsservice.repository;

import com.example.plantsnsservice.domain.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HashTagRepository extends JpaRepository<HashTag , Long> {
    Optional<HashTag> findByName(String name);
}
