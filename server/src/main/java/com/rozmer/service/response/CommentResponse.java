package com.rozmer.service.response;

import java.util.List;

import com.rozmer.service.dataobject.CommentDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CommentResponse {

	
	private List<CommentDto> content;
	private int pageNumber;
	private int pageSize;
	private long totalRecords;
	private int totalPages;	
	private boolean lastPage;
	
	
}
