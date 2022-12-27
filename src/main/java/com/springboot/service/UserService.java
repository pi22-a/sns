package com.springboot.service;

import com.springboot.domain.entity.User;
import com.springboot.dto.UserDto;
import com.springboot.dto.UserJoinRequest;
import com.springboot.exception.ApplicationException;
import com.springboot.exception.ErrorCode;
import com.springboot.repository.UserRepository;
import com.springboot.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
@Repository
public class UserService {

    private final UserRepository ur;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String secretKey;
    private long expireTime = 1000 * 60 * 60;


    public UserDto join(UserJoinRequest dto) {

        ur.findByUserName(dto.getUserName())
                .ifPresent(user -> {
                    throw new ApplicationException(ErrorCode.DUPLICATED_USER_NAME,
                            ErrorCode.DUPLICATED_USER_NAME.getMessage());
                });

        User savedUser = ur.save(dto.toEntity(encoder.encode(dto.getPassword())));

        return UserDto.builder()
                .userId(savedUser.getUserId())
                .userName(savedUser.getUserName())
                .build();
    }

    public String login(String userName, String password) {

        User user = ur.findByUserName(userName)
                .orElseThrow(() -> {
                    throw new ApplicationException(ErrorCode.NOT_FOUND_USER_NAME,
                            ErrorCode.NOT_FOUND_USER_NAME.getMessage());
                });

        if(!encoder.matches(password, user.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD,
                    ErrorCode.INVALID_PASSWORD.getMessage());
        }

        return JwtUtil.createToken(userName, secretKey, expireTime);


    }

}
