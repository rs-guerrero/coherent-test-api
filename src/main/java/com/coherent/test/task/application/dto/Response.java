package com.coherent.test.task.application.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Response {

    private String status;

    private String code;

    private Object result;

    private HttpStatus httpCode;

    private List<Error> error;
}
