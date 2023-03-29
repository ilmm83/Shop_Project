package com.shop.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "categories")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {

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

  @OneToMany(mappedBy = "parent")
  @OrderBy("name asc")
  private Set<Category> children = new HashSet<>();

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
            .collect(Collectors.toList())
        + ";\n";
  }

  @Transient
  public String getPhotosImagePath() {
    if (this.id == null || this.image == null)
      return null;

    return "/categories-images/" + this.id + "/" + this.image;
  }

}
