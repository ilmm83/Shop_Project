package com.shop.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "products")
@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, length = 256, nullable = false)
  private String name;

  @Column(unique = true, length = 256, nullable = false)
  private String alias;

  @Column(length = 512, nullable = false, name = "short_description")
  private String shortDescription;

  @Column(length = 4096, nullable = false, name = "full_description")
  private String fullDescription;

  @Column(name = "created_at")
  private Date createdAt;

  @Column(name = "updated_at")
  private Date updatedAt;

  @Column(name = "enabled")
  private boolean enabled;

  @Column(name = "in_stock")
  private boolean inStock;

  @Column(name = "price")
  private BigDecimal price;

  @Column(name = "discount_percent")
  private BigDecimal discountPercent;

  @Column(name = "cost")
  private BigDecimal cost;

  @Column(name = "length")
  private float length;

  @Column(name = "width")
  private float width;

  @Column(name = "height")
  private float height;

  @Column(name = "weight")
  private float weight;

  @Column(name = "main_image", nullable = false)
  private String mainImage;

  @ManyToOne
  @JoinColumn(name = "brand_id")
  private Brand brand;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  private List<ProductImage> images = new LinkedList<>();

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  private List<ProductDetail> details = new LinkedList<>();

  public void addExtraImage(String imageName) {
    this.images.add(new ProductImage(imageName, this));
  }

  public void addDetail(String detailName, String detailValue) {
    this.details.add(new ProductDetail(detailName, detailValue, this));
  }

  @Transient
  public String getMainImagesPath() {
    if (this.id == null || this.mainImage == null)
      return null;

    return "/product-images/" + this.id + "/" + this.mainImage;
  }

  public boolean containsImageName(String fileName) {
    for (var image : images) {
      if (image.getName().equals(fileName))
        return true;
    }
    return false;
  }
}
