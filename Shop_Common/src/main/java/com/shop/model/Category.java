package com.shop.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 128, nullable = false, unique = true)
  private String name;

  @Column(length = 64, nullable = false, unique = true)
  private String alias;

  @Column(length = 64, nullable = false)
  private String image;

  private boolean enabled;

  @OneToOne
  @JoinColumn(name = "parent_id")
  private Category parent;

  @OneToMany(mappedBy = "parent")
  private Set<Category> children = new HashSet<>();

  @Override
  public String toString() {
    return 
        "\n  id - " + id + ", \n"
        + "  Category - " + name + ", \n"
        + "  alias - " + alias + ", \n"
        + "  image - " + image + ", \n"
        + "  parent - " + parent + ", \n"
        + "  enabled - " + enabled + ", \n"
        + "  childrens\n\t" + children.stream()
            .map(kid -> kid.getName())
            .collect(Collectors.toList()) + ";\n";
  }
}
