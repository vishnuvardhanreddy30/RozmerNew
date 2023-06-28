package com.rozmer.service.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rozmer.service.entities.Qrating;
import com.rozmer.service.entities.Question;
import com.rozmer.service.entities.User;

public interface QratingRepo  extends JpaRepository<Qrating	, Integer> {
/* 
    @Query(value = "select * from qratings where question_question_id = :key", nativeQuery = true)
	List<Qrating> findByPostUsingId(@Param("key") Integer questionId);  */

    @Query(value = "select * from qratings where question_question_id = :key", nativeQuery = true) 
    List<Qrating> findByQuestionId(@Param("key") Integer questionId);

    List<Qrating> findByQuestionAndUser(Question question, User user);

/* 	Optional<Qrating> findByRatingId(Integer qratingId);  */

   /*  @Query(value = "select * from qratings where post_post_id = :postId and user_user_id =:userId", nativeQuery = true)
	List<Qrating> findByPostIdAndUserId(@Param("postId") Integer postId,@Param("userId") Integer userId);  */

}
