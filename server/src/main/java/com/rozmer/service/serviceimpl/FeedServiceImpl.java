package com.rozmer.service.serviceimpl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rozmer.service.entities.Feed;
import com.rozmer.service.repo.FeedRepository;
import com.rozmer.service.service.FeedService;

@Service
@Transactional
public class FeedServiceImpl implements FeedService {
   
   @Autowired
	FeedRepository feedRepository;

     public Feed feedDetails(Feed img , String body){
         img.setBody(body);
        return feedRepository.save(img);
     }
     
     public Optional<Feed> getFeed(String imageName){
        return feedRepository.findByTitle(imageName);
     }
}
