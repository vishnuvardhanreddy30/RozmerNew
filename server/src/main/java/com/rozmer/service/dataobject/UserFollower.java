package com.rozmer.service.dataobject;

import lombok.*;

@NoArgsConstructor
@Data
public class UserFollower {

    private Long id;

    private User follower;

    private User following;

}

