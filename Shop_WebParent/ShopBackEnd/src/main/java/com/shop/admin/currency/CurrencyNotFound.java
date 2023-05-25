package com.shop.admin.currency;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Currency not found")
public class CurrencyNotFound extends RuntimeException {
    public CurrencyNotFound(String message) {
        super(message);
    }
}
