package com.nqt.profile_service.dto.request;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileUpdateRequest {
    String firstName;
    String lastName;
    Date dob;
    String address;
}
