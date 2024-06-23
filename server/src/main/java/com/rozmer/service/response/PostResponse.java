package com.rozmer.service.response;

import java.util.List;

import com.rozmer.service.dataobject.PostDtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PostResponse {
	private List<PostDtos> articleContent;
	private List<PostDtos> poemContent;
	private int pageNumber;
	private int pageSize;
	private long totalRecords;
	private int totalPages;	
	private boolean lastPage;
}
