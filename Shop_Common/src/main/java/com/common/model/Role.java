package com.common.model;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Table(name = "roles")
@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 40, nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Override
    public String toString() {
        return name;
    }
}
