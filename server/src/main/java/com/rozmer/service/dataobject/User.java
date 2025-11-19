package com.rozmer.service.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {


    private Long userId;

    private String email;

    private String firstName;

    private String lastName;

    private String mobileNo;

    private String username;

    private Boolean following;
    private String imageName;
}
