package com.rozmer.service.repo;

import com.rozmer.service.entities.ArticleAccess;
import com.rozmer.service.entities.Post;
import com.rozmer.service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleAccessRepository extends JpaRepository<ArticleAccess,Long> {

    boolean existsByUserIdAndPostId(Long userId, Integer postId);

    boolean existsByUserAndPost(User user, Post post);
}
