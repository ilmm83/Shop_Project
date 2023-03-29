package com.shop.site.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**");

        var pathToCategoriesImages = "F:\\Projects\\JavaProjects\\Shop_Project\\Shop_WebParent\\categories-images";
        exposeDirectory(pathToCategoriesImages, "categories-images", registry);

        var pathToBrandsImages = "F:\\Projects\\JavaProjects\\Shop_Project\\Shop_WebParent\\brands-images";
        exposeDirectory(pathToBrandsImages, "brands-images", registry);

        var pathToProductImages = "F:\\Projects\\JavaProjects\\Shop_Project\\Shop_WebParent\\product-images";
        exposeDirectory(pathToProductImages, "product-images", registry);

        var pathToProductExtraImages = "F:\\Projects\\JavaProjects\\Shop_Project\\Shop_WebParent\\product-images\\extras";
        exposeDirectory(pathToProductExtraImages, "extras", registry);

        var pathToSiteLogo = "F:\\Projects\\JavaProjects\\Shop_Project\\Shop_WebParent\\site-logo";
        exposeDirectory(pathToSiteLogo, "site-logo", registry);
    }

    private void exposeDirectory(String fullPath, String folderName, ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/" + folderName + "/**")
                .addResourceLocations("file:/" + fullPath + "/");
    }

}
