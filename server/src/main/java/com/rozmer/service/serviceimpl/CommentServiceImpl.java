package com.rozmer.service.serviceimpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rozmer.service.dataobject.CommentDto;
import com.rozmer.service.entities.Comment;
import com.rozmer.service.entities.Post;
import com.rozmer.service.entities.User;
import com.rozmer.service.exception.ResourceNotFoundException;
import com.rozmer.service.repo.CommentRepo;
import com.rozmer.service.repo.PostRepo;
import com.rozmer.service.repo.UserRepository;
import com.rozmer.service.response.CommentResponse;
import com.rozmer.service.service.CommentService;

@Service
public class CommentServiceImpl
		implements CommentService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CommentRepo commentRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId, Long userId) {

		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post id ", postId));
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User ", "User id", userId));

		Comment comment = this.modelMapper.map(commentDto, Comment.class);
		comment.setPost(post);
		comment.setUser(user);
		comment.setAddedDate(new Date());
		Comment savedComment = this.commentRepo.save(comment);
		return this.modelMapper.map(savedComment, CommentDto.class);
	}

	@Override
	public void deleteComment(Integer commentId) {

		Comment com = this.commentRepo.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "CommentId", commentId));
		this.commentRepo.delete(com);
	}

	@Override
	public CommentDto updateComment(CommentDto commentDto, Integer commentId) {
		Comment comment = this.commentRepo.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment Not Exists for ", "comment ", commentId));
		comment.setContent(commentDto.getContent());
		Comment updatComment = this.commentRepo.save(comment);
		return this.modelMapper.map(updatComment, CommentDto.class);
	}

	@Override
	public List<CommentDto> getCommentsOnPost(Integer postId) {

		List<Comment> comments = this.commentRepo.findByPostUsingId(postId);
		if (comments.size() == 0) {
			throw new ResourceNotFoundException("Comment ", "postId ", postId);
		}
		List<CommentDto> commentDtos = comments.stream().map((com) -> this.modelMapper.map(com, CommentDto.class))
				.collect(Collectors.toList());

		return commentDtos;
	}

	@Override
	public CommentResponse getCommnetsByPostIds(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,
			Integer postId) {
		Pageable paging = PageRequest.of(
				pageNumber, pageSize, Sort.by("post").descending());
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Questions not found for ", "Post id", postId));
		Page<Comment> pagePost = this.commentRepo.findByPost(post, paging);

		List<Comment> allQuestion = pagePost.getContent();
		List<CommentDto> questionDtos = allQuestion.stream().map((comm) -> this.modelMapper.map(comm, CommentDto.class))
				.collect(Collectors.toList());

		CommentResponse commentResponse = new CommentResponse();

		commentResponse.setContent(questionDtos);
		commentResponse.setPageNumber(pagePost.getNumber());
		commentResponse.setPageSize(pagePost.getSize());
		commentResponse.setTotalRecords(pagePost.getTotalElements());

		commentResponse.setTotalPages(pagePost.getTotalPages());
		commentResponse.setLastPage(pagePost.isLast());

		return commentResponse;
	}

	@Override
	public CommentResponse getCommentById(Integer postId, Integer commentId) {
		Pageable paging = PageRequest.of(
				0, 10, Sort.by("post").ascending());
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment not found for ", "Post id", postId));
		Optional<Comment> pageComments = this.commentRepo.findById(commentId);

		// List<Comment> comments = pageComments.getContent();
		List<CommentDto> commentDtos = pageComments.stream().map((comm) -> this.modelMapper.map(comm, CommentDto.class))
				.collect(Collectors.toList());

		CommentResponse commentResponse = new CommentResponse();

		commentResponse.setContent(commentDtos);

		return commentResponse;
	}
}
