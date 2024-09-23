package com.nqt.message_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nqt.message_service.entity.Message;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    Page<Message> findAllByChatId(String chatId, Pageable pageable);

    Optional<Message> findBySenderIdAndRecipientId(String senderId, String recipientId);

    List<Message> findAllByChatIdAndReadStatus(String chatId, Boolean readStatus);
}
