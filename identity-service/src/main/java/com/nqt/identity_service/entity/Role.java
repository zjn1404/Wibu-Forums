package com.nqt.identity_service.entity;

import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "role")
@Table(name = "role")
public class Role {

    @Id
    @Column(name = "role_name")
    String name;

    String description;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                CascadeType.PERSIST, CascadeType.MERGE,
                CascadeType.DETACH, CascadeType.REFRESH
            })
    @JoinTable(
            name = "role_permission",
            joinColumns = {@JoinColumn(name = "role_name")},
            inverseJoinColumns = {@JoinColumn(name = "permission_name")})
    Set<Permission> permissions;
}
