package com.shop.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/resources/**");

    var path = "F:\\Projects\\JavaProjects\\Shop_Project\\Shop_WebParent\\categories-images";
    registry.addResourceHandler("/categories-images/**")
        .addResourceLocations("file:/" + path + "/");
  }

}
