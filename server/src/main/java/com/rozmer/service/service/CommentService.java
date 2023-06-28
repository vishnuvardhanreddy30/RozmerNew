package com.rozmer.service.service;

import java.util.List;

import com.rozmer.service.dataobject.CommentDto;
import com.rozmer.service.response.CommentResponse;

public interface CommentService {

	CommentDto createComment(CommentDto commentDto, Integer postId, Long userId);

	void deleteComment(Integer commentId);

	CommentDto updateComment(CommentDto commentDto, Integer commentId);

	// get all Comments on Post
	List<CommentDto> getCommentsOnPost(Integer postId);

	// get all Question by Post ID with Pagination
	public CommentResponse getCommnetsByPostIds(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,
			Integer postId);

	CommentResponse getCommentById(Integer postId, Integer commentId);

}
