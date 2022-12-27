package com.springboot.domain;

import com.springboot.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {

    private ErrorCode errorCode;
    private String message;


}