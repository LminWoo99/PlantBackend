package com.example.plantpayservice.repository;

import com.example.plantpayservice.domain.entity.OutboxEvent;
import org.springframework.data.repository.CrudRepository;

public interface OutboxEventRepository extends CrudRepository<OutboxEvent, String> {
}
