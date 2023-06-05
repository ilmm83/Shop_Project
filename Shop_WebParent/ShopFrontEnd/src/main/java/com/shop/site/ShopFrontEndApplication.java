package com.shop.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.common.model", "com.common.admin"})
public class ShopFrontEndApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopFrontEndApplication.class, args);
    }
}
