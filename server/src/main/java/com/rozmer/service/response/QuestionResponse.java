package com.rozmer.service.response;

import java.util.List;

import com.rozmer.service.dataobject.QuestionDto;

import lombok.Data;

@Data
public class QuestionResponse {
    private List<QuestionDto> content;
	private int pageNumber;
	private int pageSize;
	private long totalRecords;
	private int totalPages;	
	private boolean lastPage;

}
