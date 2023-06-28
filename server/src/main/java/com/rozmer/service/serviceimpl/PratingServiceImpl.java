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

import com.rozmer.service.dataobject.PratingGetDto;
import com.rozmer.service.dataobject.PratingPostDto;
import com.rozmer.service.entities.Post;
import com.rozmer.service.entities.Prating;
import com.rozmer.service.entities.User;
import com.rozmer.service.exception.ResourceNotFoundException;
import com.rozmer.service.repo.PostRepo;
import com.rozmer.service.repo.PratingRepo;
import com.rozmer.service.repo.UserRepository;
import com.rozmer.service.response.PratingResponse;
import com.rozmer.service.service.PratingService;

@Service
public class PratingServiceImpl
                implements PratingService {

        @Autowired
        private PostRepo postRepo;

        @Autowired
        private UserRepository userRepo;

        @Autowired
        private PratingRepo pratingRepo;

        @Autowired
        private ModelMapper modelMapper;

        /*
         * @Override
         * public PratingDto createPrating(PratingDto pratingDto, Integer postId, Long
         * userId) {
         *
         * Post post = this.postRepo.findById(postId)
         * .orElseThrow(() -> new ResourceNotFoundException("Post", "post id ",
         * postId));
         * User user = this.userRepo.findById(userId)
         * .orElseThrow(() -> new ResourceNotFoundException("User ", "User id",
         * userId));
         *
         * Prating prating = this.modelMapper.map(pratingDto, Prating.class);
         * prating.setPost(post);
         * prating.setUser(user);
         * prating.setAddedDate(new Date());
         * Prating savedPrating = this.pratingRepo.save(prating);
         * return this.modelMapper.map(savedPrating, PratingDto.class);
         * }
         *
         * @Override
         * public void deletePrating(Integer pratingId) {
         *
         * Prating com = this.pratingRepo.findById(pratingId)
         * .orElseThrow(() -> new ResourceNotFoundException("Prating", "PratingId",
         * pratingId));
         * this.pratingRepo.delete(com);
         * }
         *
         * @Override
         * public PratingDto updatePrating(PratingDto pratingDto, Integer pratingId) {
         * Prating prating = this.pratingRepo.findById(pratingId)
         * .orElseThrow(() -> new ResourceNotFoundException("Prating Not Exists for ",
         * "prating ", pratingId));
         * prating.setContent(pratingDto.getContent());
         * Prating updatPrating = this.pratingRepo.save(prating);
         * return this.modelMapper.map(updatPrating, PratingDto.class);
         *
         * }
         */

        @Override
        public List<PratingGetDto> getPratingsOnPost(Integer postId) {

                List<Prating> pratings = this.pratingRepo.findByPostUsingId(postId);
                if (pratings.size() == 0) {
                        throw new ResourceNotFoundException("Prating ", "postId ", postId);
                }
                List<PratingGetDto> pratingGetDtos = pratings.stream()
                                .map((com) -> this.modelMapper.map(com, PratingGetDto.class))
                                .collect(Collectors.toList());

                return pratingGetDtos;
        }

        @Override
        public PratingResponse getPratingsByPostIds(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,
                        Integer postId) {
                Pageable paging = PageRequest.of(
                        pageNumber, pageSize, Sort.by("post").ascending());
                Post post = this.postRepo.findById(postId)
                                .orElseThrow(() -> new ResourceNotFoundException("Pratings not found for ", "Post id",
                                                postId));
                Page<Prating> pagePost = this.pratingRepo.findByPost(post, paging);

                List<Prating> allPrating = pagePost.getContent();
                List<PratingGetDto> pratingGetDtos = allPrating.stream()
                                .map((comm) -> this.modelMapper.map(comm, PratingGetDto.class))
                                .collect(Collectors.toList());

                PratingResponse pratingResponse = new PratingResponse();

                // pratingResponse.setContent(pratingDtos);

                return pratingResponse;
        }

        /*
         * @Override
         * public PratingDto postPrating(PratingDto pratingDto, Integer postId, Long
         * userId) {
         * Prating prating = this.pratingRepo.findById(postId)
         * .orElseThrow(() -> new ResourceNotFoundException("Post", "post id ",
         * postId));
         * User user = this.userRepo.findById(userId)
         * .orElseThrow(() -> new ResourceNotFoundException("User ", "User id",
         * userId));
         *
         * Prating pratingToSave = this.modelMapper.map(pratingDto, Prating.class);
         * prating.setUser(user);
         * prating.setAddedDate(new Date());
         * Prating savedPrating = this.pratingRepo.save(pratingToSave);
         * return this.modelMapper.map(savedPrating, PratingDto.class);
         * }
         */

        @Override
        public PratingGetDto updatePrating(PratingGetDto pratingGetDto, Integer pratingId, Long userId) {

                Prating prating = this.pratingRepo.findById(pratingId)
                                .orElseThrow(() -> new ResourceNotFoundException("Prating ", "Prating id",
                                                pratingId));

                User user = this.userRepo.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User ", "User id", userId));

                Prating pratingMapped = this.modelMapper.map(pratingGetDto, Prating.class);
                prating.setRating(pratingMapped.getRating());
                prating.setUser(user);
                prating.setAddedDate(new Date());
                Prating savedPrating = this.pratingRepo.save(prating);
                return this.modelMapper.map(savedPrating, PratingGetDto.class);
        }

        @Override
        public PratingResponse getPratingByPostandUserID(Integer postId, Integer userId) {
                Post post = this.postRepo.findById(postId)
                                .orElseThrow(() -> new ResourceNotFoundException("Pratings not found for ", "Post id",
                                                postId));

                List<Prating> allPrating = this.pratingRepo.findByPostIdAndUserId(postId, userId);
                List<PratingGetDto> pratingGetDtos = allPrating.stream()
                                .map((comm) -> this.modelMapper.map(comm, PratingGetDto.class))
                                .collect(Collectors.toList());

                PratingResponse pratingResponse = new PratingResponse();

                // pratingResponse.setPrating(allPrating.get(0));
                pratingResponse.setPratingGetDto(pratingGetDtos);
                return pratingResponse;
        }

        /*
         * @Override
         * public PratingResponse getRatingById(Integer pratingId) {
         *
         * Prating prating = this.pratingRepo.findByRatingId(pratingId)
         * .orElseThrow(() -> new ResourceNotFoundException("Prating not found ",
         * "prating id", pratingId));
         *
         * Page<Prating> pagePrating = this.pratingRepo.findByPost(post, paging);
         *
         * PratingResponse pratingResponse = new PratingResponse();
         *
         * pratingResponse.setRating(prating);
         *
         * return pratingResponse;
         * }
         */

        @Override
        public PratingGetDto addOrUpdateRating(PratingPostDto pratingPostDto, Integer postId, Long userId) {
                Prating prating = this.modelMapper.map(pratingPostDto, Prating.class);
                Post p = postRepo.getReferenceById(postId);

                User u = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("Invalid user"));
                List<Prating> list = pratingRepo.findByPostAndUser(p, u);

                if (list.size() > 0 && pratingPostDto.getId() == null) {
                        prating.setId(list.get(0).getId());
                }
                prating.setPost(p);
                prating.setUser(u);
                prating.setAddedDate(new Date());

                return this.modelMapper.map(pratingRepo.save(prating), PratingGetDto.class);
        }
}
