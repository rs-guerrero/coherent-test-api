package com.coherent.test.task.infrastructure.utils;


import com.coherent.test.task.application.dto.Error;
import com.coherent.test.task.application.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

public class ControllerUtils {

    public static final String SUCCESS = "success";

    public static final String OK_CODE = "OK";

    public static final String FAIL = "fail";

    private ControllerUtils() {
    }

    public static ResponseEntity<Response> getResponseSuccessOk(Object result) {
        Response response = new Response();
        response.setResult(result);
        response.setStatus(SUCCESS);
        response.setCode(OK_CODE);
        response.setHttpCode(HttpStatus.OK);
        return new ResponseEntity<>(response, response.getHttpCode());
    }

    public static ResponseEntity<Response>  getResponseError(Error error) {
        var response = new Response();

        response.setHttpCode(error.getHttpStatus());
        response.setStatus(FAIL);
        var errors = new ArrayList<Error>();
        errors.add(error);
        response.setError(errors);

        return new ResponseEntity<>(response, error.getHttpStatus());
    }
}
