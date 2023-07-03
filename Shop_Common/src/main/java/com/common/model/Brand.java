package com.common.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "brands")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false, unique = true)
    private String name;

    @Column(length = 128, nullable = false)
    private String logo;

    @ManyToMany
    @Builder.Default
    @JoinTable(name = "brands_categories", joinColumns = @JoinColumn(name = "brand_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();


    @Override
    public String toString() {
        return "\n  id - " + id + ", \n"
            + "  Category - " + name + ", \n"
            + "  logo - " + logo + ", \n"
            + ";\n";
    }

    @Transient
    public List<String> categoriesNames() {
        var names = new ArrayList<String>();
        this.categories.forEach(cat -> names.add(cat.getName()));

        return names;
    }

    @Transient
    public String getPhotosImagePath() {
        if (this.id == null || this.logo == null) {
            return null;
        }

        return "/brands-images/" + this.id + "/" + this.logo;
    }
}
