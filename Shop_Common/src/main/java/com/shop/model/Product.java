package com.shop.model;

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
  private String shortDiscription;

  @Column(length = 4096, nullable = false, name = "full_description")
  private String fullDiscription;

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

  @ManyToOne
  @JoinColumn(name = "brand_id")
  private Brand brand;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

}
