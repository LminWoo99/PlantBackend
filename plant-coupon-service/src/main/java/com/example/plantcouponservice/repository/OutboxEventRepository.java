package com.example.plantcouponservice.repository;

import com.example.plantcouponservice.domain.OutboxEvent;
import org.springframework.data.repository.CrudRepository;

public interface OutboxEventRepository extends CrudRepository<OutboxEvent, String> {
}
