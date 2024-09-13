package com.nqt.notification_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.nqt.notification_service.entity.Notification;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    @Query("{ 'recipients_read_status.?#{[0]}': false }")
    Page<Notification> findAllUnreadNotifications(String userId, Pageable pageable);
}
