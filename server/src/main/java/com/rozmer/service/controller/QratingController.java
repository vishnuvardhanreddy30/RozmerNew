package com.rozmer.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rozmer.service.config.AppConstants;
import com.rozmer.service.dataobject.QratingDto;
import com.rozmer.service.entities.Qrating;
import com.rozmer.service.response.QratingResponse;
import com.rozmer.service.service.QratingService;

@RestController
@RequestMapping("/api/")
public class QratingController {

	@Autowired
	private QratingService qratingService;

	/*
	 * @PostMapping("/qratings/{postId}/user/{userId}")
	 * 
	 * @CrossOrigin
	 * public ResponseEntity<QratingDto> createQrating(@RequestBody QratingDto
	 * qrating, @PathVariable Integer postId,
	 * 
	 * @PathVariable Long userId) {
	 * 
	 * QratingDto createQrating = this.qratingService.postQrating(qrating, postId,
	 * userId);
	 * return new ResponseEntity<QratingDto>(createQrating, HttpStatus.CREATED);
	 * }
	 */
	/*
	 * @DeleteMapping("/qratings/{qratingId}")
	 * 
	 * @CrossOrigin
	 * public ResponseEntity<ApiResponse> deleteQrating(@PathVariable Integer
	 * qratingId) {
	 * 
	 * this.qratingService.deleteQrating(qratingId);
	 * 
	 * return new ResponseEntity<ApiResponse>(new
	 * ApiResponse("Qrating deleted successfully !!", true), HttpStatus.OK);
	 * }
	 */
	// update Qratings
	@PutMapping("/qratings/{qratingId}/user/{userId}")
	@CrossOrigin
	public ResponseEntity<QratingDto> updateQrating(@RequestBody QratingDto qrating, @PathVariable Integer qratingId,
			@PathVariable Long userId) {

		QratingDto updateQrating = this.qratingService.updateQrating(qrating, qratingId, userId);
		return new ResponseEntity<QratingDto>(updateQrating, HttpStatus.OK);

	}

	// with-pagination
	@GetMapping("/qratings/question/{questionId}")
	@CrossOrigin
	public ResponseEntity<QratingResponse> getQratingsByQuestionIds(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
			@PathVariable("postId") Integer questionId) {

		QratingResponse qratingResponse = this.qratingService.getQratingsByQuestionIds(pageNumber, pageSize, sortBy,
				sortDir, questionId);

		// getQratingsOnQuestion
		return new ResponseEntity<QratingResponse>(qratingResponse, HttpStatus.OK);
	}

	// with-pagination
	@GetMapping("/qratings/comment/{qnId}/user/{userId}")
	@CrossOrigin
	public ResponseEntity<QratingResponse> getQratingByUserId(
			@PathVariable("qnId") Integer qnId, @PathVariable("userId") Long userId) {

		QratingResponse qratingResponse = this.qratingService.getQratingByQnandUserID(qnId, userId);
		return new ResponseEntity<QratingResponse>(qratingResponse, HttpStatus.OK);
	}

	@PostMapping("/qratings/questions/{questionId}/user/{userId}")
	@CrossOrigin
	public ResponseEntity<Qrating> addOrUpdateRating(@RequestBody QratingDto qratingDto,
			@PathVariable Integer questionId,
			@PathVariable Long userId) {

		Qrating qrating = this.qratingService.addOrUpdateRating(qratingDto, questionId, userId);
		return new ResponseEntity<Qrating>(qrating, HttpStatus.CREATED);
	}
}
