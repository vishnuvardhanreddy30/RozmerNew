package com.rozmer.service.response;

import java.util.List;

import com.rozmer.service.dataobject.AnswerDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AnswerResponse {

	
	private List<AnswerDto> content;
	private int pageNumber;
	private int pageSize;
	private long totalRecords;
	private int totalPages;	
	private boolean lastPage;
	
	
}
