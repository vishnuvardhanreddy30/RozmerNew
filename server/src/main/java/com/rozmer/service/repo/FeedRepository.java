package com.rozmer.service.repo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.rozmer.service.entities.Feed;

public interface FeedRepository extends JpaRepository<Feed, Long>  {
    Optional<Feed> findByTitle(String name);    
}
