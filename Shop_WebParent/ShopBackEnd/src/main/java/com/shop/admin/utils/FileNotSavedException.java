package com.shop.admin.utils;

public class FileNotSavedException extends RuntimeException {
    public FileNotSavedException(String message, Exception e) {
        super(message, e);
    }
}
