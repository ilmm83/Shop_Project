package com.shop.admin.exception.state;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "State not found")
public class StateNotFoundException extends Exception {
    public StateNotFoundException(String message) {
        super(message);
    }
}
