package com.shop.admin.exception.country;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class CountryExceptionResponse {
    private String message;
    private long timestamp;
}
