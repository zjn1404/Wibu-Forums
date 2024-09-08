package com.nqt.profile_service.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.nqt.profile_service.entity.UserProfile;

@Repository
public interface UserProfileRepository extends Neo4jRepository<UserProfile, String> {
    Optional<UserProfile> findByUserId(String userId);

    boolean existsByUserId(String userId);

    @Query("MATCH (u:user_profile {user_id: $userId}), (f:user_profile {user_id: $friendId}) "
            + "OPTIONAL MATCH (u)-[:friend_of]-(f) "
            + "WITH u, f "
            + "MERGE (u)-[:friend_of]-(f) "
            + "RETURN EXISTS((u)-[:friend_of]-(f))")
    boolean addFriend(String userId, String friendId);

    @Query(
            value = "MATCH (u:user_profile {user_id: $userId})-[:friend_of]-(f:user_profile) "
                    + "RETURN f "
                    + "SKIP $skip LIMIT $limit",
            countQuery = "MATCH (u:user_profile {user_id: $userId})-[:friend_of]-(f:user_profile) " + "RETURN count(f)")
    Page<UserProfile> findAllFriends(String userId, Pageable pageable);

    @Query("MATCH (u:user_profile {user_id: $userId})-[r:friend_of]-(f:user_profile {user_id: $friendId}) "
            + "DELETE r "
            + "RETURN exists((u)-[:friend_of]-(f))")
    boolean unfriend(String userId, String friendId);

    @Query("MATCH (u:user_profile {user_id: $userId}), (f:user_profile {user_id: $friendId}) "
            + "RETURN EXISTS((u)-[:friend_of]-(f))")
    boolean isFriendOf(String userId, String friendId);
}
