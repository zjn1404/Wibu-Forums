package com.nqt.identity_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse <T> {

    @Value("${code.success}")
    int code;

    String message;

    T result;

    public ApiResponse(T result) {
        this.result = result;
    }
}
