package com.rozmer.service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rozmer.service.dataobject.CratingPostDto;
import com.rozmer.service.entities.Crating;
import com.rozmer.service.response.CratingResponse;

@Service
public interface CratingService {

	// Give Rating on Post
	// CratingDto postCrating(CratingDto cratingDto, Integer postId, Long userId);

	// update Rating
	CratingPostDto updateCrating(CratingPostDto cratingPostDto, Integer cratingId, Long userId);

	// get Rating
	CratingResponse getCratingByCommentandUserID(Integer commentId, Long userId);

	// get all Cratings on Post
	List<CratingPostDto> getCratingsOnPost(Integer postId);

	public CratingResponse getCratingsByPostIds(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,
			Integer postId);

	Crating addOrUpdateRating(CratingPostDto cratingPostDto, Integer commentsId, Long userId);
}
