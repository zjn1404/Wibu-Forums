package com.nqt.profile_service.service.addfriendrequest;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nqt.event.dto.Recipient;
import com.nqt.event.notification.NotificationType;
import com.nqt.profile_service.dto.request.ResponseAddFriendRequest;
import com.nqt.profile_service.dto.response.AddFriendRequestResponse;
import com.nqt.profile_service.dto.response.PageResponse;
import com.nqt.profile_service.entity.AddFriendRequest;
import com.nqt.profile_service.entity.UserProfile;
import com.nqt.profile_service.exception.AppException;
import com.nqt.profile_service.exception.ErrorCode;
import com.nqt.profile_service.mapper.AddFriendRequestMapper;
import com.nqt.profile_service.repository.AddFriendRequestRepository;
import com.nqt.profile_service.repository.UserProfileRepository;
import com.nqt.profile_service.service.kafka.KafkaProduceService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddFriendRequestServiceImp implements AddFriendRequestService {
    private final AddFriendRequestMapper addFriendRequestMapper;

    UserProfileRepository userProfileRepository;
    AddFriendRequestRepository addFriendRequestRepository;

    KafkaProduceService kafkaProduceService;

    @Override
    public void sendAddFriendRequest(String friendId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (userProfileRepository.isFriendOf(authentication.getName(), friendId)) {
            return;
        }

        AddFriendRequest addFriendRequest = AddFriendRequest.builder()
                .sendingUserId(authentication.getName())
                .receivingUserId(friendId)
                .build();

        addFriendRequestRepository.save(addFriendRequest);

        sendFriendRelatedNotification(authentication.getName(), friendId, NotificationType.ADD_FRIEND);
    }

    @Override
    @Transactional
    public void responseAddFriendRequest(ResponseAddFriendRequest request) {
        log.info(request.toString());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        addFriendRequestRepository.deleteBySendingUserIdAndReceivingUserId(
                request.getFriendId(), authentication.getName());

        if (request.isAccepted()) {
            userProfileRepository.addFriend(authentication.getName(), request.getFriendId());

            sendFriendRelatedNotification(
                    authentication.getName(), request.getFriendId(), NotificationType.ACCEPT_FRIEND);
        }
    }

    @Override
    public PageResponse<AddFriendRequestResponse> getAllAddFriendRequestsByUserId(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<AddFriendRequest> addFriendRequests =
                addFriendRequestRepository.findAllByReceivingUserId(userId, pageable);

        List<AddFriendRequestResponse> responses = addFriendRequests.getContent().stream()
                .map(addFriendRequest -> {
                    AddFriendRequestResponse addFriendRequestResponse =
                            addFriendRequestMapper.toAddFriendRequestResponse(addFriendRequest);
                    UserProfile userProfile = userProfileRepository
                            .findByUserId(addFriendRequest.getSendingUserId())
                            .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND));
                    addFriendRequestResponse.setSendingUserName(
                            String.format("%s %s", userProfile.getFirstName(), userProfile.getLastName()));

                    return addFriendRequestResponse;
                })
                .toList();

        return PageResponse.<AddFriendRequestResponse>builder()
                .currentPage(page)
                .totalPages(addFriendRequests.getTotalPages())
                .totalElements(addFriendRequests.getTotalElements())
                .pageSize(size)
                .data(responses)
                .build();
    }

    private void sendFriendRelatedNotification(String senderId, String recipientId, NotificationType notificationType) {
        UserProfile recipientProfile = userProfileRepository
                .findByUserId(recipientId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND));

        UserProfile userProfile = userProfileRepository
                .findByUserId(senderId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND));

        Recipient recipient = Recipient.builder()
                .name(recipientProfile.getFirstName() + " " + recipientProfile.getLastName())
                .userId(recipientId)
                .build();

        String body = String.format(
                "%s %s%s", userProfile.getFirstName(), userProfile.getLastName(), notificationType.getBody());

        kafkaProduceService.sendNotification(notificationType, List.of(recipient), senderId, body);
    }
}
