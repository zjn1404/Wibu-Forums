package com.nqt.identity_service.service.user;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import jakarta.transaction.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.nqt.identity_service.dto.request.user.UpdateMyInfoRequest;
import com.nqt.identity_service.dto.request.user.UserCreationRequest;
import com.nqt.identity_service.dto.request.user.UserUpdateRequest;
import com.nqt.identity_service.dto.request.userprofile.UserProfileCreationRequest;
import com.nqt.identity_service.dto.response.UserResponse;
import com.nqt.identity_service.entity.Role;
import com.nqt.identity_service.entity.User;
import com.nqt.identity_service.exception.AppException;
import com.nqt.identity_service.exception.ErrorCode;
import com.nqt.identity_service.mapper.UserMapper;
import com.nqt.identity_service.mapper.UserProfileMapper;
import com.nqt.identity_service.repository.RoleRepository;
import com.nqt.identity_service.repository.UserRepository;
import com.nqt.identity_service.repository.profileservice.ProfileClient;
import com.nqt.identity_service.service.verifycode.VerifyCodeService;
import com.nqt.identity_service.utils.Utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImp implements UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    ProfileClient profileClient;

    VerifyCodeService verifyCodeService;

    Utils utils;

    UserMapper userMapper;
    UserProfileMapper userProfileMapper;

    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse createUser(UserCreationRequest request) {
        User user = createInternalUser(request);
        verifyCodeService.sendVerifyMail(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public User createInternalUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        User user = userMapper.toUser(request);
        user.setId(utils.buildUserId(user));
        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (!Objects.isNull(request.getRoles())) {
            List<Role> roles = roleRepository.findAllById(request.getRoles());
            user.setRoles(new HashSet<>(roles));
        }

        user = userRepository.save(user);

        UserProfileCreationRequest userCreationRequest = userProfileMapper.toUserProfileCreationRequest(request);
        userCreationRequest.setUserId(user.getId());
        profileClient.createUserProfile(userCreationRequest);

        return user;
    }

    @Override
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);

        if (!Objects.isNull(request.getRoles())) {
            List<Role> roles = roleRepository.findAllById(request.getRoles());
            user.setRoles(new HashSet<>(roles));
        }

        if (!Objects.isNull(request.getPassword())) {
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new AppException(ErrorCode.THE_SAME_PASSWORD);
            }

            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateMyInfo(UpdateMyInfoRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!verifyCodeService.verify(request.getVerifyCode(), authentication.getName())) {
            throw new AppException(ErrorCode.VERIFY_CODE_INCORRECT);
        }

        User user = userRepository
                .findById(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateMyInfo(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getUserById(String userId) {
        return userMapper.toUserResponse(
                userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    @Override
    public UserResponse getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository
                .findById(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        UserResponse userResponse = userMapper.toUserResponse(user);
        userResponse.setNoPassword(!StringUtils.hasText(user.getPassword()));
        return userResponse;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    @Transactional
    public void deleteUserById(String userId) {
        verifyCodeService.deleteVerifyCode(userId);
        userRepository.deleteById(userId);
    }
}
