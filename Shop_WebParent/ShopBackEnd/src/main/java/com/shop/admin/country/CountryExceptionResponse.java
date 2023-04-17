package com.shop.admin.country;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class CountryExceptionResponse {
    private String message;
    private long timestamp;
}
