package com.rozmer.service.service;

import java.util.Optional;
import com.rozmer.service.entities.Feed;

public interface FeedService {

    public Feed feedDetails(Feed img, String body);
    public Optional<Feed> getFeed(String imageName);
    
}
