package com.mongoDB.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class HumanException extends RuntimeException {

    public HumanException(String message){
        super(message);
    }
}
