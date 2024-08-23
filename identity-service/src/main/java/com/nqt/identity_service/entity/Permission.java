package com.nqt.identity_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "permission")
@Table(name = "permission")
public class Permission {

    @Id
    @Column(name = "permission_name")
    String name;

    @Column(name = "description")
    String description;
}
