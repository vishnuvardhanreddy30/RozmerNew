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

import com.rozmer.service.dataobject.CratingGetDto;
import com.rozmer.service.dataobject.CratingPostDto;
import com.rozmer.service.entities.Comment;
import com.rozmer.service.entities.Crating;
import com.rozmer.service.entities.Post;
import com.rozmer.service.entities.User;
import com.rozmer.service.exception.ResourceNotFoundException;
import com.rozmer.service.repo.CommentRepo;
import com.rozmer.service.repo.CratingRepo;
import com.rozmer.service.repo.PostRepo;
import com.rozmer.service.repo.UserRepository;
import com.rozmer.service.response.CratingResponse;
import com.rozmer.service.service.CratingService;

@Service
public class CratingServiceImpl implements CratingService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CratingRepo cratingRepo;

	@Autowired
	private CommentRepo commentRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CratingResponse getCratingsByPostIds(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,
			Integer postId) {
		Pageable paging = PageRequest.of(
				pageNumber, pageSize, Sort.by("post").ascending());
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Cratings not found for ", "Post id", postId));
		Page<Crating> pagePost = (Page<Crating>) this.cratingRepo.findByPostUsingId(postId);
		// Page<Crating> pagePost = this.cratingRepo.findByPost(post, paging);

		List<Crating> allCrating = pagePost.getContent();
		List<CratingGetDto> cratingPostDtos = allCrating.stream()
				.map((comm) -> this.modelMapper.map(comm, CratingGetDto.class))
				.collect(Collectors.toList());

		CratingResponse cratingResponse = new CratingResponse();

		cratingResponse.setCrating(cratingPostDtos);

		return cratingResponse;
	}

	/*
	 * @Override
	 * public CratingDto postCrating(CratingDto cratingDto, Integer postId, Long
	 * userId) {
	 * Crating crating = this.cratingRepo.findById(postId)
	 * .orElseThrow(() -> new ResourceNotFoundException("Post", "post id ",
	 * postId));
	 * User user = this.userRepo.findById(userId)
	 * .orElseThrow(() -> new ResourceNotFoundException("User ", "User id",
	 * userId));
	 * 
	 * Crating cratingToSave = this.modelMapper.map(cratingDto, Crating.class);
	 * crating.setUser(user);
	 * crating.setAddedDate(new Date());
	 * Crating savedCrating = this.cratingRepo.save(cratingToSave);
	 * return this.modelMapper.map(savedCrating, CratingDto.class);
	 * }
	 */

	@Override
	public CratingPostDto updateCrating(CratingPostDto cratingPostDto, Integer cratingId, Long userId) {

		Crating crating = this.cratingRepo.findById(cratingId)
				.orElseThrow(() -> new ResourceNotFoundException("Crating ", "Crating id",
						cratingId));

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User ", "User id", userId));

		Crating cratingMapped = this.modelMapper.map(cratingPostDto, Crating.class);
		crating.setRating(cratingMapped.getRating());
		crating.setUser(user);
		crating.setAddedDate(new Date());
		Crating savedCrating = this.cratingRepo.save(crating);
		return this.modelMapper.map(savedCrating, CratingPostDto.class);
	}

	@Override
	public CratingResponse getCratingByCommentandUserID(Integer commentsId, Long userId) {
		/*
		 * Post post = this.postRepo.findById(postId)
		 * .orElseThrow(() -> new ResourceNotFoundException("Cratings not found for ",
		 * "Post id", postId));
		 */
		Comment comment = commentRepo.getReferenceById(commentsId);
		User u = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("Invalid user"));

		List<Crating> allCrating = cratingRepo.findByCommentAndUser(comment, u);
		List<CratingGetDto> cratingGetDtos = allCrating.stream()
				.map((comm) -> this.modelMapper.map(comm, CratingGetDto.class))
				.collect(Collectors.toList());

		CratingResponse cratingResponse = new CratingResponse();

		cratingResponse.setCrating(cratingGetDtos);

		return cratingResponse;
	}

	@Override
	public Crating addOrUpdateRating(CratingPostDto cratingPostDto, Integer commentsId, Long userId) {
		Crating crating = this.modelMapper.map(cratingPostDto, Crating.class);
		Comment comment = commentRepo.getReferenceById(commentsId);
		crating.setComment(comment);

		User u = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("Invalid user"));
		crating.setUser(u);
		List<Crating> list = cratingRepo.findByCommentAndUser(comment, u);

		if (list.size() > 0 && cratingPostDto.getId() == null) {
			crating.setId(list.get(0).getId());
		}
		crating.setAddedDate(new Date());
		Crating savedCrating = cratingRepo.save(crating);
		savedCrating.setUser(null);
		savedCrating.setComment(null);
		return savedCrating;
	}

	@Override
	public List<CratingPostDto> getCratingsOnPost(Integer postId) {
		return null;
	}
}
