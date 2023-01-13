package com.shop.admin.config;

import java.nio.file.Path;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    String dirName = "user-images";
    Path userImagesDir = Path.of("f:/Projects/JavaProject/Shop_Project/Shop_WebParent/Shop_BackEnd/src/main/resources/static/images/" + dirName);
    String userImagesPath = userImagesDir.toString();
    

    registry.addResourceHandler("/resources/**");
  }

}
