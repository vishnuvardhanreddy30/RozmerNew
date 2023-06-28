package com.rozmer.service.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rozmer.service.entities.Answer;
import com.rozmer.service.entities.Question;
import com.rozmer.service.entities.User;

public interface AnswerRepo extends JpaRepository<Answer,Integer>  {

    Page<Answer> findByQuestion(Question question, Pageable p);

    Page<Answer> findByUser(User user , Pageable p);
    
}
