package com.rozmer.service.repo;

import com.rozmer.service.entities.User;
import com.rozmer.service.entities.UserFollower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserFollowerRepository extends JpaRepository<UserFollower, Long> {
    List<UserFollower> findByFollowerAndFollowing(User follower, User following);
    void deleteByFollowerAndFollowing(User follower, User following);

    List<UserFollower> findByFollower(User follower);
    List<UserFollower> findByFollowing(User following);

    List<UserFollower> findByFollower(Long followerId);
}

