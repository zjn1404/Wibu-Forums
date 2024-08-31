package com.nqt.identity_service.service.verifycode;

import com.nqt.identity_service.entity.User;

public interface VerifyCodeService {

    void verify(String code, String userId);

    void sendVerifyMail(User user);

    void deleteVerifyCode(String userId);
}
