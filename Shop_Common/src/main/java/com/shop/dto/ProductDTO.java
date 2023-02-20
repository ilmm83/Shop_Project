package com.shop.dto;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

  private Long brandId;

  private Long categoryId;

  public String getMainImagesPath() {
    if (this.id == null || this.mainImage == null)
      return null;

    return "/product-images/" + this.id + "/" + this.mainImage;
  }
}
