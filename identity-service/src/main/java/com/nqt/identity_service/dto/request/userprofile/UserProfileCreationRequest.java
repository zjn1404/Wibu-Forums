package com.nqt.identity_service.dto.request.userprofile;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileCreationRequest {
    String userId;
    String firstName;
    String lastName;
    Date dob;
    String address;
}
