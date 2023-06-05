package com.shop.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.shop.model", "com.shop.admin"})
public class ShopFrontEndApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopFrontEndApplication.class, args);
    }
}
