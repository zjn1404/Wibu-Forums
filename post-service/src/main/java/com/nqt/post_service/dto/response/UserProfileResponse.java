package com.nqt.post_service.dto.response;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    String id;
    String userId;
    String firstName;
    String lastName;
    Date dob;
    String address;
}
