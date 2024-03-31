package com.example.plantcouponservice.repository;

import com.example.plantcouponservice.domain.FailedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailedEventRepository extends JpaRepository<FailedEvent, Integer> {

}
