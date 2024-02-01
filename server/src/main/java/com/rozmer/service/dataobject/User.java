package com.rozmer.service.dataobject;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class User {

    private Long userId;

    private String email;

    private String firstName;

    private String lastName;

    private String mobileNo;

    private String username;

    private Boolean following;
}
