package com.common.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Table(name = "products")
@Entity
@Getter
@Setter
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

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> images = new LinkedList<>();

    @Builder.Default
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

    @Transient
    public BigDecimal getDiscountPrice() {
        BigDecimal dp = discountPercent.divide(new BigDecimal(100));
        return this.price.stripTrailingZeros()
                .subtract(this.price.stripTrailingZeros().multiply(dp));
    }

    @Transient
    public String getDiscPercent() {
        var sb = new StringBuilder(String.valueOf(discountPercent));
        int s = sb.indexOf("."), e = sb.length();

        while (e-- > s) { sb.deleteCharAt(e); }

        return sb.toString();
    }

    public boolean containsImageName(String fileName) {
        for (var image : images) {
            if (image.getName().equals(fileName))
                return true;
        }
        return false;
    }
}
