package com.example.plantchatservice.repository.mongo;

import com.example.plantchatservice.domain.mongo.Chatting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoChatRepository extends MongoRepository<Chatting, String> {

    List<Chatting> findByChatRoomNo(Integer chatNo);

    Page<Chatting> findByChatRoomNoOrderBySendDateDesc(Integer chatRoomNo, Pageable pageable);
}
