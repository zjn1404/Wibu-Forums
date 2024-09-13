package com.nqt.profile_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.nqt.profile_service.entity.AddFriendRequest;

@Repository
public interface AddFriendRequestRepository extends Neo4jRepository<AddFriendRequest, String> {

    Page<AddFriendRequest> findAllByReceivingUserId(String receivingUserId, Pageable pageable);

    void deleteBySendingUserIdAndReceivingUserId(String sendingUserId, String receivingUserId);
}
