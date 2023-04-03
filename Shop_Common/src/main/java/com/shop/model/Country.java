package com.shop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Table(name = "countries")
@Entity
@NoArgsConstructor
public class Country {

    public Country(Integer id) {
        this.id = id;
    }

    public Country(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Country(int id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Integer id;

    @Column(nullable = false, length = 45)
    @Getter @Setter
    private String name;

    @Column(nullable = false, length = 5)
    @Getter @Setter
    private String code;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    private Set<State> states;

    @OneToMany(mappedBy = "country")
    private Set<Customer> customers;
}
