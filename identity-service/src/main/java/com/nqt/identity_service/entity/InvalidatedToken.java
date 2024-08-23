package com.nqt.identity_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "invalidated_token")
@Table(name = "invalidated_token")
public class InvalidatedToken {

    @Id
    @Column(name = "ac_id")
    @Setter(AccessLevel.NONE)
    String acId;

    @Column(name = "rf_id")
    @Setter(AccessLevel.NONE)
    String rfId;

    @Column(name = "expiry_time")
    Date expiryTime;
}
