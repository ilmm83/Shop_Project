package com.shop.admin.exception.currency;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Currency not found")
public class CurrencyNotFound extends Exception {
    public CurrencyNotFound(String message) {
        super(message);
    }
}
