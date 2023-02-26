package com.shop.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.shop.model.ProductDetail;
import com.shop.model.ProductImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  private List<ProductDetail> details = new LinkedList<>();

  private List<ProductImage> images = new LinkedList<>();

  private Long brandId;

  private Long categoryId;

  public String getMainImagesPath() {
    if (this.id == null || this.mainImage == null)
      return null;

    return "/product-images/" + this.id + "/" + this.mainImage;
  }
}
