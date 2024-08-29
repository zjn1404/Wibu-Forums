package com.nqt.identity_service.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OutboundUserResponse {
    String id;
    String email;
    boolean verifiedEmail;
    String name;
    String givenName;
    String familyName;
    String picture;
    String locale;

    @Override
    public String toString() {
        return "OutboundUserResponse{" + "id='"
                + id + '\'' + ", email='"
                + email + '\'' + ", verifiedEmail="
                + verifiedEmail + ", name='"
                + name + '\'' + ", givenName='"
                + givenName + '\'' + ", familyName='"
                + familyName + '\'' + ", picture='"
                + picture + '\'' + ", locale='"
                + locale + '\'' + '}';
    }
}
