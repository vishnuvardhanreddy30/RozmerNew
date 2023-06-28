package com.rozmer.service.serviceimpl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.rozmer.service.dataobject.QuestionDto;
import com.rozmer.service.entities.Answer;
import com.rozmer.service.entities.Post;
import com.rozmer.service.entities.Question;
import com.rozmer.service.entities.User;
import com.rozmer.service.exception.ResourceNotFoundException;
import com.rozmer.service.repo.AnswerRepo;
import com.rozmer.service.repo.PostRepo;
import com.rozmer.service.repo.QuestionRepo;
import com.rozmer.service.repo.UserRepository;
import com.rozmer.service.response.QuestionResponse;
import com.rozmer.service.service.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class QuestionServiceImpl implements QuestionService{

	@Autowired
	PostRepo postRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	QuestionRepo questionRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AnswerRepo answerRepo;

	@Override
    public QuestionDto postQuestion(QuestionDto questionDto ,Integer postId ,Long userId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Postsnot found for ", "User id", postId));
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User is not availaible for ", "User id", userId));

		Question question = this.modelMapper.map(questionDto, Question.class);
		question.setPost(post);
		question.setUser(user);
		question.setAddedDate(new Date());

		Question newQuestion = this.questionRepo.save(question);
		return this.modelMapper.map(newQuestion, QuestionDto.class);
	}

	@Override
	public QuestionResponse getAllQuestion(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

		Pageable paging = PageRequest.of(
				pageNumber, pageSize, Sort.by("questionId").ascending());
		Page<Question> pageQuestion = questionRepo.findAll(paging);

		List<Question> allPosts = pageQuestion.getContent();

		List<QuestionDto> postDtos = allPosts.stream().map((post) -> this.modelMapper.map(post, QuestionDto.class))
				.collect(Collectors.toList());

		QuestionResponse questionResponse = new QuestionResponse();

		questionResponse.setContent(postDtos);
		questionResponse.setPageNumber(pageQuestion.getNumber());
		questionResponse.setPageSize(pageQuestion.getSize());
		questionResponse.setTotalRecords(pageQuestion.getTotalElements());

		questionResponse.setTotalPages(pageQuestion.getTotalPages());
		questionResponse.setLastPage(pageQuestion.isLast());

		return questionResponse;
	}

	@Override
	public List<QuestionDto> getQuestionByPostId(Integer postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Questions not found for ", "Post id", postId));

		List<Question> questions = this.questionRepo.findByPost(post);

		List<QuestionDto> postDtos = questions.stream().map((question) -> this.modelMapper.map(question, QuestionDto.class))
				.collect(Collectors.toList());

		return postDtos;
	}

	@Override
	public QuestionResponse getQuestionByPostIds(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,Integer postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Questions not found for ", "Post id", postId));

		Pageable paging = PageRequest.of(
				pageNumber, pageSize, Sort.by("questionId").ascending());
		Page<Question> pagePost = this.questionRepo.findByPost(post, paging);

		List<Question> allQuestion = pagePost.getContent();
		List<QuestionDto> questionDtos = allQuestion.stream().map((que) -> this.modelMapper.map(que, QuestionDto.class))
				.collect(Collectors.toList());

		QuestionResponse postResponse = new QuestionResponse();

		postResponse.setContent(questionDtos);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalRecords(pagePost.getTotalElements());

		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());

		return postResponse;
	}

	@Override
	public QuestionResponse getQuestionsById(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,Integer postId, Integer qnId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Questions not found for ", "Post id", postId));

		Pageable paging = PageRequest.of(
				pageNumber, pageSize, Sort.by("questionId").ascending());
		Page<Question> pagePost = this.questionRepo.findByQuestionId(qnId, paging);

		List<Question> allQuestion = pagePost.getContent();
		List<QuestionDto> questionDtos = allQuestion.stream().map((que) -> this.modelMapper.map(que, QuestionDto.class))
				.collect(Collectors.toList());

		QuestionResponse postResponse = new QuestionResponse();

		postResponse.setContent(questionDtos);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalRecords(pagePost.getTotalElements());

		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());

		return postResponse;
	}

	@Override
	public void deleteQuestion(Integer questionId) {
		Question question = this.questionRepo.findById(questionId)
				.orElseThrow(() -> new ResourceNotFoundException("Question ", "Question id", questionId));
		Answer answer = this.answerRepo.findById(questionId)
				.orElse(null);

		if(!ObjectUtils.isEmpty(answer)){
			this.answerRepo.delete(answer);
		}

		if(!ObjectUtils.isEmpty(question)){
			this.questionRepo.delete(question);
		}

	}

}
