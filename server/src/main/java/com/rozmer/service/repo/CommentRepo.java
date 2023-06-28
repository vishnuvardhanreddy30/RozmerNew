package com.rozmer.service.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rozmer.service.entities.Comment;
import com.rozmer.service.entities.Post;

public interface CommentRepo  extends JpaRepository<Comment	, Integer> {

    @Query(value = "select * from comments where post_post_id = :key", nativeQuery = true)
	List<Comment> findByPostUsingId(@Param("key") Integer postId); 

    Page<Comment> findByPost(Post post,Pageable p);

	Optional<Comment> findById(Integer commentId); 

}
