package com.rozmer.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rozmer.service.config.AppConstants;
import com.rozmer.service.dataobject.CommentDto;
import com.rozmer.service.response.ApiResponse;
import com.rozmer.service.response.CommentResponse;
import com.rozmer.service.service.CommentService;

@RestController
@RequestMapping("/api/")
public class CommentController {

	@Autowired
	private CommentService commentService;

	@PostMapping("/post/{postId}/comments/user/{userId}")
	@CrossOrigin
	public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto comment, @PathVariable Integer postId,
			@PathVariable Long userId) {

		CommentDto createComment = this.commentService.createComment(comment, postId, userId);
		return new ResponseEntity<CommentDto>(createComment, HttpStatus.CREATED);
	}

	@DeleteMapping("/comments/{commentId}")
	@CrossOrigin
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId) {

		this.commentService.deleteComment(commentId);

		return new ResponseEntity<ApiResponse>(new ApiResponse("Comment deleted successfully !!", true), HttpStatus.OK);
	}

	// update Comments
	@PutMapping("/comments/{commentId}")
	@CrossOrigin
	public ResponseEntity<CommentDto> updateComment(@RequestBody CommentDto comment, @PathVariable Integer commentId) {

		CommentDto updateComment = this.commentService.updateComment(comment, commentId);
		return new ResponseEntity<CommentDto>(updateComment, HttpStatus.OK);

	}

	// with-pagination
	@GetMapping("/comments/post/{postId}")
	@CrossOrigin
	public ResponseEntity<CommentResponse> getCommentsonPost(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
			@PathVariable("postId") Integer postId) {

		CommentResponse commentResponse = this.commentService.getCommnetsByPostIds(pageNumber, pageSize, sortBy,
				sortDir, postId);
		return new ResponseEntity<CommentResponse>(commentResponse, HttpStatus.OK);
	}

	// with-pagination
	@GetMapping("/comments/post/{postId}/{commentId}")
	@CrossOrigin
	public ResponseEntity<CommentResponse> getCommentById(
			@PathVariable("postId") Integer postId, @PathVariable("commentId") Integer commentId) {

		CommentResponse commentResponse = this.commentService.getCommentById(postId, commentId);
		return new ResponseEntity<CommentResponse>(commentResponse, HttpStatus.OK);
	}

}
