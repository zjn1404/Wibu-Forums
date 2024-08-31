package com.nqt.identity_service.entity;

import java.util.Date;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity(name = "verify_code")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerifyCode {

    @Id
    @Column(name = "verify_code")
    String verifyCode;

    @Column(name = "expiry_time")
    Date expiryTime;

    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = {
                CascadeType.PERSIST, CascadeType.MERGE,
                CascadeType.DETACH, CascadeType.REFRESH
            })
    @JoinColumn(name = "user_id")
    User user;
}
