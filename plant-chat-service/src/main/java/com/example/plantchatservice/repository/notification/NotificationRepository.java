package com.example.plantchatservice.repository.notification;

import com.example.plantchatservice.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "SELECT * FROM Notification n " +
            "WHERE (n.url, n.reg_date)" +
            "IN (SELECT url, max(reg_date) " +
            "FROM NOTIFICATION "+
            "WHERE type = 'CHAT' "+
            "GROUP BY url) "+
            "AND n.receiver_no =:memberNo", nativeQuery = true)
    List<Notification> findChatByReceiver(Integer memberNo);
}
