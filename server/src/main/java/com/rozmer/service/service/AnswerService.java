package com.rozmer.service.service;

import com.rozmer.service.dataobject.AnswerDto;
import com.rozmer.service.response.AnswerResponse;

public interface AnswerService {

    AnswerDto postAnswer(AnswerDto answerDto, Integer questionId ,Long userId);

	//get all Answer by Question ID with Pagination
    public AnswerResponse getAnswersByQuestionIds(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,Integer questionId);

    //get all Answer by Question ID with Pagination for UserID
    public AnswerResponse getAnswersByQuestionByUserids(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,Long userId);

    // delete

	void deleteAnswer(Integer answerId);
    
}
