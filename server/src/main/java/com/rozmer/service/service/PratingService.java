package com.rozmer.service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rozmer.service.dataobject.PratingGetDto;
import com.rozmer.service.dataobject.PratingPostDto;
import com.rozmer.service.response.PratingResponse;

@Service
public interface PratingService {

	// Give Rating on Post
	// PratingDto postPrating(PratingDto pratingDto, Integer postId, Long userId);

	// update Rating
	PratingGetDto updatePrating(PratingGetDto pratingGetDto, Integer pratingId, Long userId);

	// get Rating
	PratingResponse getPratingByPostandUserID(Integer postId, Integer userId);

	// get all Pratings on Post
	List<PratingGetDto> getPratingsOnPost(Integer postId);

	// get all Question by Post ID with Pagination
	public PratingResponse getPratingsByPostIds(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,
			Integer postId);

	PratingGetDto addOrUpdateRating(PratingPostDto pratingPostDto, Integer postId, Long userId);
}
