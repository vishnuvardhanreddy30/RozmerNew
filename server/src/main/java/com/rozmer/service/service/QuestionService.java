package com.rozmer.service.service;

import java.util.List;

import com.rozmer.service.dataobject.QuestionDto;
import com.rozmer.service.response.QuestionResponse;

public interface QuestionService {

    //create 

	QuestionDto postQuestion(QuestionDto questionDto , Integer postId, Long userId);
    //get all posts
	
	QuestionResponse getAllQuestion(Integer pageNumber,Integer pageSize,String sortBy,String sortDir);

    //get all Question by Post ID 
	List<QuestionDto> getQuestionByPostId(Integer postId);

    //get all Question by Post ID with Pagination
    public QuestionResponse getQuestionByPostIds(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,Integer postId);
    
    public QuestionResponse getQuestionsById(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,Integer postId, Integer qnId);

    // delete
	void deleteQuestion(Integer questionId);
    
}
