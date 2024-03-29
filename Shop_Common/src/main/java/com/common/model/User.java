package com.common.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 128, nullable = false, unique = true)
    private String email;

    @Column(length = 64, nullable = false)
    private String password;

    @Column(name = "first_name", length = 45, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 45, nullable = false)
    private String lastName;

    @Column(length = 64)
    private String photos;

    private Boolean enabled;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    public void addRole(Role role) {
        this.roles.add(role);
    }

    @Override
    public String toString() {
        var listOfRoleNames = roles.stream().map(Role::getName).toList();
        return "User: \n"
            + "  " + id + ", \n"
            + "  " + email + ", \n"
            + "  " + password + ", \n"
            + "  " + firstName + ", \n"
            + "  " + lastName + ", \n"
            + "  " + enabled + ", \n"
            + "  " + listOfRoleNames + ";\n";
    }

    @Transient
    public String getPhotosImagePath() {
        if (this.id == null || this.photos == null) {
            return null;
        }

        return "/images/user-images/" + this.id + "/" + this.photos;
    }

    @Transient
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public boolean hasRole(String roleName) {
        for (var role : roles) {
            if (role.getName().equals(roleName)) {
                return true;
            }
        }

        return false;
    }
}
