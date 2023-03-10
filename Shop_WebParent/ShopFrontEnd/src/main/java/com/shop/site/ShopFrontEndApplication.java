package com.shop.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScans(value = {@ComponentScan("com.shop"), @ComponentScan("com.shop.site")})
public class ShopFrontEndApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopFrontEndApplication.class, args);
    }
}
