package com.rozmer.service.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rozmer.service.entities.Post;
import com.rozmer.service.entities.Prating;
import com.rozmer.service.entities.User;

@Repository
public interface PratingRepo extends JpaRepository<Prating, Integer> {

    @Query(value = "select * from pratings where post_post_id = :key", nativeQuery = true)
    List<Prating> findByPostUsingId(@Param("key") Integer postId);

    Page<Prating> findByPost(Post post, Pageable p);

    // Optional<Prating> findByRatingId(Integer pratingId);

    @Query(value = "select * from pratings where post_post_id = :postId and user_user_id =:userId", nativeQuery = true)
    List<Prating> findByPostIdAndUserId(@Param("postId") Integer postId, @Param("userId") Integer userId);

    List<Prating> findByPostAndUser(Post postId, User userId);

}
