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
import com.rozmer.service.dataobject.PratingGetDto;
import com.rozmer.service.dataobject.PratingPostDto;
import com.rozmer.service.response.PratingResponse;
import com.rozmer.service.service.PratingService;

@RestController
@RequestMapping("/api/")
public class PratingController {

	@Autowired
	private PratingService pratingService;

	/*
	 * @PostMapping("/pratings/{postId}/user/{userId}")
	 * 
	 * @CrossOrigin
	 * public ResponseEntity<PratingDto> createPrating(@RequestBody PratingDto
	 * prating, @PathVariable Integer postId,
	 * 
	 * @PathVariable Long userId) {
	 * 
	 * PratingDto createPrating = this.pratingService.postPrating(prating, postId,
	 * userId);
	 * return new ResponseEntity<PratingDto>(createPrating, HttpStatus.CREATED);
	 * }
	 */
	/*
	 * @DeleteMapping("/pratings/{pratingId}")
	 * 
	 * @CrossOrigin
	 * public ResponseEntity<ApiResponse> deletePrating(@PathVariable Integer
	 * pratingId) {
	 * 
	 * this.pratingService.deletePrating(pratingId);
	 * 
	 * return new ResponseEntity<ApiResponse>(new
	 * ApiResponse("Prating deleted successfully !!", true), HttpStatus.OK);
	 * }
	 */
	// update Pratings
	@PutMapping("/pratings/{pratingId}/user/{userId}")
	@CrossOrigin
	public ResponseEntity<PratingGetDto> updatePrating(@RequestBody PratingGetDto prating,
			@PathVariable Integer pratingId,
			@PathVariable Long userId) {

		PratingGetDto updatePrating = this.pratingService.updatePrating(prating, pratingId, userId);
		return new ResponseEntity<PratingGetDto>(updatePrating, HttpStatus.OK);

	}

	// with-pagination
	@GetMapping("/pratings/post/{postId}")
	@CrossOrigin
	public ResponseEntity<PratingResponse> getPratingsOnPost(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
			@PathVariable("postId") Integer postId) {

		PratingResponse pratingResponse = this.pratingService.getPratingsByPostIds(pageNumber, pageSize, sortBy,
				sortDir, postId);
		return new ResponseEntity<PratingResponse>(pratingResponse, HttpStatus.OK);
	}

	// with-pagination
	@GetMapping("/pratings/post/{postId}/user/{userId}")
	@CrossOrigin
	public ResponseEntity<PratingResponse> getpratingByUserId(
			@PathVariable("postId") Integer postId, @PathVariable("userId") Integer userId) {

		PratingResponse pratingResponse = this.pratingService.getPratingByPostandUserID(postId, userId);
		return new ResponseEntity<PratingResponse>(pratingResponse, HttpStatus.OK);
	}

	@PostMapping("/pratings/post/{postId}/user/{userId}")
	@CrossOrigin
	public ResponseEntity<PratingGetDto> addOrUpdateRating(@RequestBody PratingPostDto pratingPostDto,
			@PathVariable Integer postId,
			@PathVariable Long userId) {

		PratingGetDto pratingGetDto = this.pratingService.addOrUpdateRating(pratingPostDto, postId, userId);
		return new ResponseEntity<>(pratingGetDto, HttpStatus.CREATED);
	}

}
