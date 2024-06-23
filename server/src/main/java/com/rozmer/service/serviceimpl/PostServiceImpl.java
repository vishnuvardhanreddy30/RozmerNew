package com.rozmer.service.serviceimpl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.var;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.rozmer.service.dataobject.PostDto;
import com.rozmer.service.dataobject.PostDtos;
import com.rozmer.service.entities.Post;
import com.rozmer.service.entities.User;
import com.rozmer.service.exception.ResourceNotFoundException;
import com.rozmer.service.repo.PostRepo;
import com.rozmer.service.repo.UserRepository;
import com.rozmer.service.response.PostResponse;
import com.rozmer.service.service.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userRepo;

	static final String ARTICLE = "article";
	static final String POEM = "poem";

	@Override
	public PostDto createPost(PostDto postDto, Long userId) {

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User ", "User id", userId));

		Post post = this.modelMapper.map(postDto, Post.class);
		post.setImageName("");
		post.setAddedDate(new Date());
		post.setUser(user);

		Post newPost = this.postRepo.save(post);

		return this.modelMapper.map(newPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {

		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post ", "post id", postId));

		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());

		Post updatedPost = this.postRepo.save(post);
		return this.modelMapper.map(updatedPost, PostDto.class);
	}

	@Override
	public void deletePost(Integer postId) {

		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post ", "post id", postId));

		this.postRepo.delete(post);

	}

	@Override
	public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable p = PageRequest.of(pageNumber, pageSize, sort);

		Page<Post> pagePost = this.postRepo.findAll(p);

		List<Post> allPosts = pagePost.getContent();

		List<PostDtos> postDtos = allPosts.stream().map((post) -> this.modelMapper.map(post, PostDtos.class))
				.collect(Collectors.toList());

		return getPostResponse(postDtos, pagePost);
	}

	@Override
	public PostDto getPostById(Integer postId) {
		Post post = this.postRepo.findById(postId)
		//Post post = this.postRepo.findByPostUsingId(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));

		PostDto returnPostDto = this.modelMapper.map(post, PostDto.class);

		return returnPostDto;
	}

	@Override
	public List<PostDto> getPostsByUser(Long userId) {

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User ", "userId ", userId));
		List<Post> posts = this.postRepo.findByUser(user);

		List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		return postDtos;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		List<Post> posts = this.postRepo.searchByTitle("%" + keyword + "%");
		List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		if (ObjectUtils.isEmpty(postDtos)) {

		}
		return postDtos;
	}

	@Override
	public PostResponse searchPostByTitleResponse(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,
			String keyword) {

		List<Post> posts = this.postRepo.searchByTitle("%" + keyword + "%");

		List<PostDtos> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDtos.class))
				.collect(Collectors.toList());
		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable p = PageRequest.of(pageNumber, pageSize, sort);

		Page<Post> pagePost = this.postRepo.searchByTitlewithPage("%" + keyword + "%", p);

		return getPostResponse(postDtos, pagePost);
	}

	private PostResponse getPostResponse(List<PostDtos> postDtos, Page<Post> pagePost) {
		PostResponse postResponse = new PostResponse();

		var postArticleDtos = postDtos.stream().filter(post -> post.getCategory().equals(ARTICLE)).collect(Collectors.toList());
		var postPoemDtos = postDtos.stream().filter(post -> post.getCategory().equals(POEM)).collect(Collectors.toList());

		postResponse.setArticleContent(postArticleDtos);
		postResponse.setPoemContent(postPoemDtos);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalRecords(pagePost.getTotalElements());

		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());

		return postResponse;
	}

}
