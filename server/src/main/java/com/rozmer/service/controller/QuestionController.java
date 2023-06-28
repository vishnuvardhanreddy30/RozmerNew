package com.rozmer.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.rozmer.service.dataobject.QuestionDto;
import com.rozmer.service.entities.Question;
import com.rozmer.service.repo.QuestionRepo;
import com.rozmer.service.response.ApiResponse;
import com.rozmer.service.response.QuestionResponse;
import com.rozmer.service.service.QuestionService;

@RestController
@RequestMapping("/ask-question")
public class QuestionController {

	@Autowired
	private QuestionService questionService;

	@Autowired
	QuestionRepo questionRepo;

	// POST QUESTION
	@PostMapping("/post/{postId}/question/user/{userId}")
	@CrossOrigin
	public ResponseEntity<QuestionDto> createPost(@RequestBody QuestionDto questionDto,
			@PathVariable Integer postId, @PathVariable Long userId) {
		QuestionDto createQuestion = this.questionService.postQuestion(questionDto, postId, userId);
		return new ResponseEntity<QuestionDto>(createQuestion, HttpStatus.CREATED);
	}

	@GetMapping("/questions")
	@CrossOrigin
	public ResponseEntity<QuestionResponse> getAllPost(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {

		QuestionResponse questionResponse = this.questionService.getAllQuestion(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.OK);
	}

	@GetMapping("/page-wise")
	@CrossOrigin
	public List<Question> getPageOne() {

		// First page with 5 items
		Pageable paging = PageRequest.of(
				0, 5, Sort.by("user").ascending());
		Page<Question> page = questionRepo.findAll(paging);

		// Retrieve the items
		return page.getContent();
	}

	// with-pagination
	@GetMapping("/question/{postId}")
	@CrossOrigin
	public ResponseEntity<QuestionResponse> getQuestionsByPost(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
			@PathVariable("postId") Integer postId) {

		QuestionResponse questionResponse = this.questionService.getQuestionByPostIds(pageNumber, pageSize, sortBy,
				sortDir, postId);
		return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.OK);
	}

	// with-pagination
	@GetMapping("/question/{postId}/{qnId}")
	@CrossOrigin
	public ResponseEntity<QuestionResponse> getQuestionsById(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
			@PathVariable("postId") Integer postId, @PathVariable("qnId") Integer qnId) {

		QuestionResponse questionResponse = this.questionService.getQuestionsById(pageNumber, pageSize, sortBy,
				sortDir, postId, qnId);
		return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.OK);
	}

	// delete Question
	@DeleteMapping("/question/{questionId}")
	@CrossOrigin
	public ApiResponse deletePost(@PathVariable Integer questionId) {
		this.questionService.deleteQuestion(questionId);
		return new ApiResponse("Question is successfully deleted !!", true);
	}

}
