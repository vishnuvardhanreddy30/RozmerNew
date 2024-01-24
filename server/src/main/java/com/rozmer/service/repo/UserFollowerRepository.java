package com.rozmer.service.repo;

import com.rozmer.service.entities.User;
import com.rozmer.service.entities.UserFollower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFollowerRepository extends JpaRepository<UserFollower, Long> {
    List<UserFollower> findByFollowerAndFollowing(User follower, User following);
    void deleteByFollowerAndFollowing(User follower, User following);
}

