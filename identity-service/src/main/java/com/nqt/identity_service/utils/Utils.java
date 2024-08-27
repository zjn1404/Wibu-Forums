package com.nqt.identity_service.utils;

import com.nqt.identity_service.entity.User;

public class Utils {

    public static String buildUserId(User user) {
        return String.format("%s%s", user.getUsername(), user.getPhoneNumber());
    }

    private Utils() {}
}
