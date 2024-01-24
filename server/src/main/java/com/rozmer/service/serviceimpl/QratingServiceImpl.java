package com.rozmer.service.serviceimpl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rozmer.service.dataobject.QratingDto;
import com.rozmer.service.entities.Qrating;
import com.rozmer.service.entities.Question;
import com.rozmer.service.entities.User;
import com.rozmer.service.exception.ResourceNotFoundException;
import com.rozmer.service.repo.QratingRepo;
import com.rozmer.service.repo.QuestionRepo;
import com.rozmer.service.repo.UserRepository;
import com.rozmer.service.response.QratingResponse;
import com.rozmer.service.service.QratingService;

@Service
public class QratingServiceImpl implements QratingService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private QratingRepo qratingRepo;

	@Autowired
	private QuestionRepo questionRepo;

	@Autowired
	private ModelMapper modelMapper;

	/*
	 * @Override
	 * public QratingDto createQrating(QratingDto qratingDto, Integer postId, Long
	 * userId) {
	 * 
	 * Post post = this.postRepo.findById(postId)
	 * .orElseThrow(() -> new ResourceNotFoundException("Post", "post id ",
	 * postId));
	 * User user = this.userRepo.findById(userId)
	 * .orElseThrow(() -> new ResourceNotFoundException("User ", "User id",
	 * userId));
	 * 
	 * Qrating qrating = this.modelMapper.map(qratingDto, Qrating.class);
	 * qrating.setPost(post);
	 * qrating.setUser(user);
	 * qrating.setAddedDate(new Date());
	 * Qrating savedQrating = this.qratingRepo.save(qrating);
	 * return this.modelMapper.map(savedQrating, QratingDto.class);
	 * }
	 * 
	 * @Override
	 * public void deleteQrating(Integer qratingId) {
	 * 
	 * Qrating com = this.qratingRepo.findById(qratingId)
	 * .orElseThrow(() -> new ResourceNotFoundException("Qrating", "QratingId",
	 * qratingId));
	 * this.qratingRepo.delete(com);
	 * }
	 * 
	 * @Override
	 * public QratingDto updateQrating(QratingDto qratingDto, Integer qratingId) {
	 * Qrating qrating = this.qratingRepo.findById(qratingId)
	 * .orElseThrow(() -> new ResourceNotFoundException("Qrating Not Exists for ",
	 * "qrating ", qratingId));
	 * qrating.setContent(qratingDto.getContent());
	 * Qrating updatQrating = this.qratingRepo.save(qrating);
	 * return this.modelMapper.map(updatQrating, QratingDto.class);
	 * 
	 * }
	 */

	@Override
	public List<QratingDto> getQratingsOnQuestion(Integer questionId) {

		List<Qrating> qratings = this.qratingRepo.findByQuestionId(questionId);
		if (qratings.size() == 0) {
			throw new ResourceNotFoundException("Qrating ", "questionId ", questionId);
		}
		List<QratingDto> qratingDtos = qratings.stream().map((com) -> this.modelMapper.map(com, QratingDto.class))
				.collect(Collectors.toList());

		return qratingDtos;
	}

	@Override
	public QratingResponse getQratingsByQuestionIds(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,
			Integer questionId) {
		/*
		 * Pageable paging = PageRequest.of(
		 * pageNumber, pageSize, Sort.by("post").ascending());
		 */
		/*
		 * Post post = this.postRepo.findById(postId)
		 * .orElseThrow(() -> new ResourceNotFoundException("Qratings not found for ",
		 * "Post id", postId));
		 */
		List<Qrating> pagePost = this.qratingRepo.findByQuestionId(questionId);

		// List<Qrating> allQrating = pagePost.getContent();
		List<QratingDto> qratingDtos = pagePost.stream().map((comm) -> this.modelMapper.map(comm, QratingDto.class))
				.collect(Collectors.toList());

		QratingResponse qratingResponse = new QratingResponse();

		qratingResponse.setQrating(qratingDtos);

		return qratingResponse;
	}

	/*
	 * @Override
	 * public QratingDto postQrating(QratingDto qratingDto, Integer postId, Long
	 * userId) {
	 * Qrating qrating = this.qratingRepo.findById(postId)
	 * .orElseThrow(() -> new ResourceNotFoundException("Post", "post id ",
	 * postId));
	 * User user = this.userRepo.findById(userId)
	 * .orElseThrow(() -> new ResourceNotFoundException("User ", "User id",
	 * userId));
	 * 
	 * Qrating qratingToSave = this.modelMapper.map(qratingDto, Qrating.class);
	 * qrating.setUser(user);
	 * qrating.setAddedDate(new Date());
	 * Qrating savedQrating = this.qratingRepo.save(qratingToSave);
	 * return this.modelMapper.map(savedQrating, QratingDto.class);
	 * }
	 */

	@Override
	public QratingDto updateQrating(QratingDto qratingDto, Integer qratingId, Long userId) {

		Qrating qrating = this.qratingRepo.findById(qratingId)
				.orElseThrow(() -> new ResourceNotFoundException("Qrating ", "Qrating id",
						qratingId));

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User ", "User id", userId));

		Qrating qratingMapped = this.modelMapper.map(qratingDto, Qrating.class);
		qrating.setRating(qratingMapped.getRating());
		qrating.setUser(user);
		qrating.setAddedDate(new Date());
		Qrating savedQrating = this.qratingRepo.save(qrating);
		return this.modelMapper.map(savedQrating, QratingDto.class);
	}

	@Override
	public Qrating addOrUpdateRating(QratingDto qratingDto, Integer questionId, Long userId) {
		Qrating qrating = this.modelMapper.map(qratingDto, Qrating.class);
		Question question = questionRepo.getReferenceById(questionId);
		qrating.setQuestion(question);

		User u = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("Invalid user"));
		qrating.setUser(u);
		List<Qrating> list = qratingRepo.findByQuestionAndUser(question, u);

		if (list.size() > 0 && qratingDto.getId() == null) {
			qrating.setId(list.get(0).getId());
		}
		qrating.setAddedDate(new Date());
		Qrating savedQrating = qratingRepo.save(qrating);
		savedQrating.setUser(null);
		savedQrating.setQuestion(null);
		return savedQrating;
	}

	@Override
	public QratingResponse getQratingByQnandUserID(Integer qnId, Long userId) {

		Question qn = questionRepo.getReferenceById(qnId);
		User u = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("Invalid user"));

		List<Qrating> allQrating = qratingRepo.findByQuestionAndUser(qn, u);
		List<QratingDto> qratingDtos = allQrating.stream()
				.map((comm) -> this.modelMapper.map(comm, QratingDto.class))
				.collect(Collectors.toList());

		QratingResponse qratingResponse = new QratingResponse();

		qratingResponse.setQrating(qratingDtos);

		return qratingResponse;
	}

	/*
	 * @Override
	 * public QratingResponse getQratingByPostandUserID(Integer postId, Integer
	 * userId) {
	 * Post post = this.postRepo.findById(postId)
	 * .orElseThrow(() -> new ResourceNotFoundException("Qratings not found for ",
	 * "Post id", postId));
	 * 
	 * List<Qrating> allQrating = this.qratingRepo.findByPostIdAndUserId(postId,
	 * userId);
	 * List<QratingDto> qratingDtos = allQrating.stream().map((comm) ->
	 * this.modelMapper.map(comm, QratingDto.class))
	 * .collect(Collectors.toList());
	 * 
	 * QratingResponse qratingResponse = new QratingResponse();
	 * 
	 * qratingResponse.setContent(qratingDtos);
	 * 
	 * return qratingResponse;
	 * }
	 */

	/*
	 * @Override
	 * public QratingResponse getRatingById(Integer qratingId) {
	 * 
	 * Qrating qrating = this.qratingRepo.findByRatingId(qratingId)
	 * .orElseThrow(() -> new ResourceNotFoundException("Qrating not found ",
	 * "qrating id", qratingId));
	 * 
	 * Page<Qrating> pageQrating = this.qratingRepo.findByPost(post, paging);
	 * 
	 * QratingResponse qratingResponse = new QratingResponse();
	 * 
	 * qratingResponse.setRating(qrating);
	 * 
	 * return qratingResponse;
	 * }
	 */

}
