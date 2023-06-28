package com.rozmer.service.service;

import java.util.List;

import com.rozmer.service.dataobject.PostDto;
import com.rozmer.service.response.PostResponse;

public interface PostService {

	//create 
	PostDto createPost(PostDto postDto,Long userId);

	//update 
	PostDto updatePost(PostDto postDto, Integer postId);

	// delete

	void deletePost(Integer postId);
	
	//get all posts
	PostResponse getAllPost(Integer pageNumber,Integer pageSize,String sortBy,String sortDir);

	//get single post
	PostDto getPostById(Integer postId);
	
	//get all posts by user
	List<PostDto> getPostsByUser(Long userId);
	
	//search posts
	List<PostDto> searchPosts(String keyword);

	//Search Post Response
	 PostResponse searchPostByTitleResponse(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,String keyword);

}
