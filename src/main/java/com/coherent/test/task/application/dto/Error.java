package com.coherent.test.task.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class Error {

    private String message;

    private HttpStatus httpStatus;

}
