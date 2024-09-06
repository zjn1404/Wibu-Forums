package com.nqt.post_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nqt.post_service.entity.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    boolean existsById(String Postid);

    Page<Post> findAllByUserId(String userId, Pageable pageable);
}
