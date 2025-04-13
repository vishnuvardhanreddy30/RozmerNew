package com.rozmer.service.controller;

import com.rozmer.service.config.AppConstants;
import com.rozmer.service.dataobject.PostDto;
import com.rozmer.service.response.ApiResponse;
import com.rozmer.service.response.PostResponse;
import com.rozmer.service.service.FileService;
import com.rozmer.service.service.PostService;
import com.rozmer.service.service.UserService;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	UserService userService;

	@Autowired
	private FileService fileService;

	@Value("${project.image.posts}")
	private String path;
	// create

	@PostMapping("/user/{userId}/posts")
	@CrossOrigin
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable Long userId) {
		PostDto createPost = this.postService.createPost(postDto, userId);
		return new ResponseEntity<>(createPost, HttpStatus.CREATED);
	}

	// get by user

	@GetMapping("/user/{userId}/posts")
	@CrossOrigin
	public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable Long userId) {

		List<PostDto> posts = this.postService.getPostsByUser(userId);
		return new ResponseEntity<>(posts, HttpStatus.OK);

	}

	// get all posts

	@GetMapping("/posts")
	@CrossOrigin
	public ResponseEntity<PostResponse> getAllPost(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
			@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "userId", required = false) Long userId
	) {

		PostResponse postResponse = this.postService.getAllPost(pageNumber, pageSize, sortBy, sortDir, category, userId);
		return new ResponseEntity<>(postResponse, HttpStatus.OK);
	}

	// get post details by id
	/* @GetMapping("/posts/{postId}")
	@CrossOrigin
	public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {
		PostDto postDto = null;
		try {
			postDto = this.postService.getPostById(postId);
			ResponseEntity<PostDto>  a = new ResponseEntity<PostDto>(postDto, HttpStatus.OK);
			return a;
		} catch (Error e) {
			System.out.println(e.toString());
		}

		return null;

	} */
	@GetMapping("/posts/{postId}")
	@CrossOrigin
	public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {
			PostDto postDto = this.postService.getPostById(postId);
			return new ResponseEntity<>(postDto, HttpStatus.OK);
	}

	// delete post
	@DeleteMapping("/posts/{postId}")
	@CrossOrigin
	public ApiResponse deletePost(@PathVariable Integer postId) {
		this.postService.deletePost(postId);
		return new ApiResponse("Post is successfully deleted !!", true);
	}

	// update post
	@PutMapping("/posts/{postId}")
	@CrossOrigin
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer postId) {

		PostDto updatePost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<>(updatePost, HttpStatus.OK);

	}

	// search
	@GetMapping("/posts/search/{keywords}")
	@CrossOrigin
	public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable("keywords") String keywords) {
		List<PostDto> result = this.postService.searchPosts(keywords);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// post image upload

	@PostMapping("/post/image/upload/{postId}")
	@CrossOrigin
	public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image,
			@PathVariable Integer postId) throws IOException {

		PostDto postDto = this.postService.getPostById(postId);

		String fileName = this.fileService.uploadImage(path, image);
		postDto.setImageName(fileName);
		PostDto updatePost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<>(updatePost, HttpStatus.OK);

	}

	// method to serve files
	@GetMapping(value = "/post/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	@CrossOrigin
	public void downloadImage(
			@PathVariable("imageName") String imageName,
			HttpServletResponse response) throws IOException {

		InputStream resource = this.fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());

	}

	// Search Response Chnages
	// search
	// @GetMapping("posts/search-by-title/{keywords}")
	// @CrossOrigin
	// public ResponseEntity<List<PostResponse>>
	// searchPostByTitleResponse(@PathVariable("keywords") String keywords) {
	// List<PostResponse> result =
	// this.postService.searchPostByTitleResponse(keywords);
	// return new ResponseEntity<List<PostResponse>>(result, HttpStatus.OK);
	// }

	@GetMapping("posts/search-by-title/{keywords}")
	@CrossOrigin
	public ResponseEntity<PostResponse> searchPostByTitleResponse(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
			@PathVariable("keywords") String keywords) {

		PostResponse postResponse = this.postService.searchPostByTitleResponse(pageNumber, pageSize, sortBy, sortDir,
				keywords);
		return new ResponseEntity<>(postResponse, HttpStatus.OK);
	}

	@PostMapping("/posts/{postId}/unlock")
	@CrossOrigin
	public ResponseEntity<String> unlockPost(@PathVariable Integer postId, @RequestParam Long userId) {
		boolean success = this.postService.unlockPost(postId, userId);
		return ResponseEntity.ok(success ? "Unlocked" : "Already unlocked");
	}
}
