package com.example.plantsnsservice.repository;

import com.example.plantsnsservice.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findImageBySnsPostId(Long snsPostId);
}
