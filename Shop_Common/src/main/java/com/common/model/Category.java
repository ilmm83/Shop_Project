package com.common.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 128, unique = true, nullable = false)
    private String name;

    @Column(length = 64, unique = true, nullable = false)
    private String alias;

    @Column(length = 64, nullable = false)
    private String image;

    private boolean enabled;

    @Column(name = "all_parent_ids", length = 256, nullable = true)
    private String allParentIDs;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @Builder.Default
    @OrderBy("name asc")
    @OneToMany(mappedBy = "parent")
    private Set<Category> children = new HashSet<>();


    public Category(Long id) {
        this.id = id;
    }

    public Category(String name, Category parent) {
        this(name);
        this.parent = parent;
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(String name) {
        this.name = name;
        this.alias = name;
        this.image = "default.png";
    }

    @Override
    public String toString() {
        return "\n  id - " + id + ", \n"
            + "  Category - " + name + ", \n"
            + "  alias - " + alias + ", \n"
            + "  image - " + image + ", \n"
            + "  parent - " + parent + ", \n"
            + "  enabled - " + enabled + ", \n"
            + "  children\n\t" + children.stream()
            .map(Category::getName)
            .toList()
            + ";\n";
    }

    @Transient
    public String getPhotosImagePath() {
        if (this.id == null || this.image == null) {
            return null;
        }

        return "/categories-images/" + this.id + "/" + this.image;
    }
}
