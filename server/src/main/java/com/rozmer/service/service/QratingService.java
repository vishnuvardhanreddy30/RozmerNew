package com.rozmer.service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rozmer.service.dataobject.QratingDto;
import com.rozmer.service.entities.Qrating;
import com.rozmer.service.response.QratingResponse;

@Service
public interface QratingService {

	// Give Rating on Post
	// QratingDto postQrating(QratingDto qratingDto, Integer postId, Long userId);

	// update Rating
	QratingDto updateQrating(QratingDto qratingDto, Integer qratingId, Long userId);

	/*
	 * // get Rating
	 * QratingResponse getQratingByPostandUserID(Integer postId, Integer userId);
	 */

	// get all Qratings on Post
	List<QratingDto> getQratingsOnQuestion(Integer questionId);

	// get all Question by Post ID with Pagination
	public QratingResponse getQratingsByQuestionIds(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,
			Integer questionId);

	Qrating addOrUpdateRating(QratingDto qratingPostDto, Integer postId, Long userId);

	// get Rating
	QratingResponse getQratingByQnandUserID(Integer qnId, Long userId);

}
