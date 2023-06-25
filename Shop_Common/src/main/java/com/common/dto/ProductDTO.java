package com.common.dto;

import com.common.model.ProductDetail;
import com.common.model.ProductImage;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {

    private Long id;

    private String name;

    private String alias;

    private String shortDescription;

    private String fullDescription;

    private Date createdAt;

    private Date updatedAt;

    private boolean enabled;

    private boolean inStock;

    private BigDecimal price;

    private BigDecimal discountPercent;

    private BigDecimal cost;

    private float length;

    private float width;

    private float height;

    private float weight;

    private String mainImage;

    @Builder.Default
    private List<ProductDetail> details = new LinkedList<>();

    @Builder.Default
    private List<ProductImage> images = new LinkedList<>();

    private Long brandId;

    private Long categoryId;


    public String getMainImagesPath() {
        if (this.id == null || this.mainImage == null) {
            return null;
        }

        return "/product-images/" + this.id + "/" + this.mainImage;
    }
}
