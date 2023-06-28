package com.rozmer.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rozmer.service.config.AppConstants;
import com.rozmer.service.dataobject.AnswerDto;
import com.rozmer.service.response.AnswerResponse;
import com.rozmer.service.response.ApiResponse;
import com.rozmer.service.service.AnswerService;

@RestController
@RequestMapping("/answer-question/")
public class AnswerController {

	@Autowired
	private AnswerService answerService;

	@PostMapping("/question/{questionId}/answer/user/{userId}")
	@CrossOrigin
	public ResponseEntity<AnswerDto> postAnswer(@RequestBody AnswerDto answerDto, @PathVariable Integer questionId, @PathVariable Long userId) {

		AnswerDto postanAnswer = this.answerService.postAnswer(answerDto, questionId , userId);
		return new ResponseEntity<AnswerDto>(postanAnswer, HttpStatus.CREATED);
	}

	//with-pagination
	@GetMapping("/answers/question/{questionId}")
	@CrossOrigin
	public  ResponseEntity<AnswerResponse> getAnswersforQuestion(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
			@PathVariable("questionId") Integer questionId) {

        AnswerResponse answerResponse = this.answerService.getAnswersByQuestionIds(pageNumber, pageSize, sortBy, sortDir ,questionId);
		return new  ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.OK);
		}


		//with-pagination
	@GetMapping("/answers/user/{userId}")
	@CrossOrigin
	public  ResponseEntity<AnswerResponse> getAnswersforQuestionByUserId(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
			@PathVariable("userId") Long userId) {

        AnswerResponse answerResponse = this.answerService.getAnswersByQuestionByUserids(pageNumber, pageSize, sortBy, sortDir ,userId);
		return new  ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.OK);
		}

		// delete post
	@DeleteMapping("/answer/{answerId}")
	@CrossOrigin
	public ApiResponse deletePost(@PathVariable Integer answerId) {
		this.answerService.deleteAnswer(answerId);
		return new ApiResponse("Post is successfully deleted !!", true);
	}

}
