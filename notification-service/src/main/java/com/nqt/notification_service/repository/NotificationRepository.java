package com.nqt.notification_service.repository;

import com.nqt.notification_service.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    @Query("{ 'recipientsReadStatus.?0': false }")
    Page<Notification> findAllUnreadNotifications(String userId, Pageable pageable);

}
