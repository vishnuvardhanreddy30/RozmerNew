package com.rozmer.service.serviceimpl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rozmer.service.dataobject.AnswerDto;
import com.rozmer.service.entities.Answer;
import com.rozmer.service.entities.Question;
import com.rozmer.service.entities.User;
import com.rozmer.service.exception.ResourceNotFoundException;
import com.rozmer.service.repo.AnswerRepo;
import com.rozmer.service.repo.QuestionRepo;
import com.rozmer.service.repo.UserRepository;
import com.rozmer.service.response.AnswerResponse;
import com.rozmer.service.service.AnswerService;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerRepo answerRepo;

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
	private ModelMapper modelMapper;

    @Override
    public AnswerDto postAnswer(AnswerDto answerDto, Integer questionId, Long userId) {
        Question question = this.questionRepo.findById(questionId)
        .orElseThrow(() -> new ResourceNotFoundException("Answer is not availaible for ", "Question id ", questionId));
        User user = this.userRepo.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User is not availaible for ", "User id", userId));
        Answer answer = this.modelMapper.map(answerDto, Answer.class);
        answer.setQuestion(question);
        answer.setUser(user);
        answer.setAddedDate(new Date());
        Answer savedAnswer = this.answerRepo.save(answer);
        return this.modelMapper.map(savedAnswer, AnswerDto.class);
    }

    @Override
    public AnswerResponse getAnswersByQuestionIds(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,
            Integer questionId) {
        Answer answer = this.answerRepo.findById(questionId)
				.orElseThrow(() -> new ResourceNotFoundException("Answer not found for ", "question id", questionId));
		Pageable paging = PageRequest.of(
                pageNumber, pageSize, Sort.by("question").ascending());
        Question question = this.questionRepo.findById(questionId)
            .orElseThrow(() -> new ResourceNotFoundException("Answer is not availaible for ", "Question id ", questionId));
        Page<Answer> pageAnswer = this.answerRepo.findByQuestion(question, paging);

		List<Answer> allQuestion = pageAnswer.getContent();
		List<AnswerDto> answerDtos = allQuestion.stream().map((ans) -> this.modelMapper.map(ans, AnswerDto.class))
				.collect(Collectors.toList());
		
		AnswerResponse answerResponse = new AnswerResponse();

		answerResponse.setContent(answerDtos);
		answerResponse.setPageNumber(pageAnswer.getNumber());
		answerResponse.setPageSize(pageAnswer.getSize());
		answerResponse.setTotalRecords(pageAnswer.getTotalElements());

		answerResponse.setTotalPages(pageAnswer.getTotalPages());
		answerResponse.setLastPage(pageAnswer.isLast());

		return answerResponse;
    }

    @Override
    public AnswerResponse getAnswersByQuestionByUserids(Integer pageNumber, Integer pageSize, String sortBy,
            String sortDir, Long userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User is not availaible for ", "User id", userId));
		Pageable paging = PageRequest.of(
                pageNumber, pageSize, Sort.by("question").ascending());
        Page<Answer> pageAnswer = this.answerRepo.findByUser(user, paging);

		List<Answer> allQuestion = pageAnswer.getContent();
		List<AnswerDto> answerDtos = allQuestion.stream().map((ans) -> this.modelMapper.map(ans, AnswerDto.class))
				.collect(Collectors.toList());
		
		AnswerResponse answerResponse = new AnswerResponse();

		answerResponse.setContent(answerDtos);
		answerResponse.setPageNumber(pageAnswer.getNumber());
		answerResponse.setPageSize(pageAnswer.getSize());
		answerResponse.setTotalRecords(pageAnswer.getTotalElements());

		answerResponse.setTotalPages(pageAnswer.getTotalPages());
		answerResponse.setLastPage(pageAnswer.isLast());

		return answerResponse;
    }

    @Override
    public void deleteAnswer(Integer answerId) {

		Answer answer = this.answerRepo.findById(answerId)
				.orElseThrow(() -> new ResourceNotFoundException("Answer not for  ", "answerid", answerId));

		this.answerRepo.delete(answer);

    }
    
}
