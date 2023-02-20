package com.shop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "product_details")
@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetail {

  public ProductDetail(String name, String value, Product product) {
    this.name = name;
    this.value = value;
    this.product = product;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 255)
  private String name;

  @Column(nullable = false, length = 255)
  private String value;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;
}
