package com.shop.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "brands")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Brand {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 45, nullable = false, unique = true)
  private String name;

  @Column(length = 128, nullable = false)
  private String logo;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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
    if (this.id == null || this.logo == null)
      return null;

    return "/logos-images/" + this.id + "/" + this.logo;
  }

}
