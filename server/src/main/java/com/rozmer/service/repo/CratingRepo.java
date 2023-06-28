package com.rozmer.service.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rozmer.service.entities.Comment;
import com.rozmer.service.entities.Crating;
import com.rozmer.service.entities.User;

public interface CratingRepo extends JpaRepository<Crating, Integer> {

    @Query(value = "select * from cratings where post_post_id = :key", nativeQuery = true)
    List<Crating> findByPostUsingId(@Param("key") Integer postId);

    // Page<Crating> findByPost(Post post,Pageable p);

    // Optional<Crating> findByRatingId(Integer cratingId);

    @Query(value = "select * from cratings where post_post_id = :postId and user_user_id =:userId", nativeQuery = true)
    List<Crating> findByPostIdAndUserId(@Param("postId") Integer postId, @Param("userId") Integer userId);

    List<Crating> findByCommentAndUser(Comment comment, User user);

}
