package com.springboot.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Builder
public class UserLoginRequest {

    private String userName;
    private String password;
}
