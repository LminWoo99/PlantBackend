package com.example.plantchatservice.repository;

import com.example.plantchatservice.dto.redis.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
//단순 crud용이므로 CrudRepository사용
@Repository
public interface ChatRoomRepository extends CrudRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByChatroomNoAndUsername(Long chatRoomNo, String username);

    List<ChatRoom> findByChatroomNo(Long chatRoomNo);

}
