package com.rozmer.service.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rozmer.service.entities.Post;
import com.rozmer.service.entities.User;

public interface PostRepo extends JpaRepository<Post, Integer> {

	List<Post> findByUser(User user);
	
	@Query("select p from Post p where p.title like :key")
	List<Post> searchByTitle(@Param("key") String title);

	@Query("select p from Post p where p.postId like :key")
	// @Query("select p.postId, p.content,r.RATING from Post p, RATINGS r where p.postId=r.postId and p.postId like :key")
	Optional<Post> findByPostUsingId(@Param("key") Integer postId);  

	@Query("select p from Post p where p.title like :key")
	Page<Post> searchByTitlewithPage(@Param("key") String title , Pageable p);

	/* @Query(value = "select p.post_id, p.content,r.rating from posts p join ratings r where p.post_id=r.post_post_id",nativeQuery = true)
	List<Post> findByPostId(Integer postId); */

	

}
