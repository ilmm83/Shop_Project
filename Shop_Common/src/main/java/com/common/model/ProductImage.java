package com.common.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    public ProductImage(String imageName, Product product) {
        this.name = imageName;
        this.product = product;
    }

    @Transient
    public String getExtraImagePath() {
        return "/product-images/" + product.getId() + "/extras/" + this.name;
    }
}
