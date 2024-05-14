package com.example.plantchatservice.repository.notification;

import com.example.plantchatservice.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "SELECT * FROM notification n " +
            "WHERE (n.url, n.reg_date)" +
            "IN (SELECT url, max(reg_date) " +
            "FROM notification "+
            "GROUP BY url) "+
            "AND n.receiver_no =:memberNo", nativeQuery = true)
    List<Notification> findByReceiver(Integer memberNo);

    //자동으로 영속성 컨텍스트를 clear(조회시 바로 db)
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Notification n SET n.isRead = 1 WHERE n.notifiNo = :id")
    void updateIsReadById(Long id);
}
