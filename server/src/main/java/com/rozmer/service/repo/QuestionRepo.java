package com.rozmer.service.repo;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rozmer.service.entities.Post;
import com.rozmer.service.entities.Question;
import com.rozmer.service.entities.User;

public interface QuestionRepo extends JpaRepository<Question , Integer> {

    @Query(value = "SELECT * FROM question p WHERE  p.post_post_id is not null", nativeQuery = true)
	Page<Question> getQuewithPage(Pageable p);

    List<Question> findByPost(Post post);

    List<Question> findByUser(User user);

    Page<Question> findByPost(Post post,Pageable p);

   Page<Question> findByQuestionId(Integer qnId, Pageable p);
}
