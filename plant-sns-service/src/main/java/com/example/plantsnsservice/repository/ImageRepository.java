package com.example.plantsnsservice.repository;

import com.example.plantsnsservice.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
