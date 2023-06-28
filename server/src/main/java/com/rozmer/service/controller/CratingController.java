package com.rozmer.service.controller;

import com.rozmer.service.entities.Crating;
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
import com.rozmer.service.dataobject.CratingPostDto;
import com.rozmer.service.response.CratingResponse;
import com.rozmer.service.service.CratingService;

@RestController
@RequestMapping("/api/")
public class CratingController {

	@Autowired
	private CratingService cratingService;

	// update Cratings
	@PutMapping("/cratings/{cratingId}/user/{userId}")
	@CrossOrigin
	public ResponseEntity<CratingPostDto> updateCrating(@RequestBody CratingPostDto crating,
			@PathVariable Integer cratingId,
			@PathVariable Long userId) {

		CratingPostDto updateCrating = this.cratingService.updateCrating(crating, cratingId, userId);
		return new ResponseEntity<CratingPostDto>(updateCrating, HttpStatus.OK);

	}

	// with-pagination
	@GetMapping("/cratings/post/{postId}")
	@CrossOrigin
	public ResponseEntity<CratingResponse> getCratingsOnPost(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
			@PathVariable("postId") Integer postId) {

		CratingResponse cratingResponse = this.cratingService.getCratingsByPostIds(pageNumber, pageSize, sortBy,
				sortDir, postId);
		return new ResponseEntity<CratingResponse>(cratingResponse, HttpStatus.OK);
	}

	// with-pagination
	@GetMapping("/cratings/comment/{commentId}/user/{userId}")
	@CrossOrigin
	public ResponseEntity<CratingResponse> getcratingByUserId(
			@PathVariable("commentId") Integer commentId, @PathVariable("userId") Long userId) {

		CratingResponse cratingResponse = this.cratingService.getCratingByCommentandUserID(commentId, userId);
		return new ResponseEntity<CratingResponse>(cratingResponse, HttpStatus.OK);
	}

	@PostMapping("/cratings/comments/{commentsId}/user/{userId}")
	@CrossOrigin
	public ResponseEntity<Crating> addOrUpdateRating(@RequestBody CratingPostDto cratingPostDto,
			@PathVariable Integer commentsId,
			@PathVariable Long userId) {

		Crating crating = this.cratingService.addOrUpdateRating(cratingPostDto, commentsId, userId);
		return new ResponseEntity<Crating>(crating, HttpStatus.CREATED);
	}

}
