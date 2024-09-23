package com.nqt.message_service.service.message;

import java.io.IOException;
import java.util.*;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nqt.event.dto.Recipient;
import com.nqt.event.notification.NotificationType;
import com.nqt.message_service.dto.request.SaveMessageRequest;
import com.nqt.message_service.dto.response.MessageResponse;
import com.nqt.message_service.dto.response.PageResponse;
import com.nqt.message_service.entity.Message;
import com.nqt.message_service.exception.AppException;
import com.nqt.message_service.exception.ErrorCode;
import com.nqt.message_service.mapper.MessageMapper;
import com.nqt.message_service.repository.MessageRepository;
import com.nqt.message_service.service.chatroom.ChatRoomService;
import com.nqt.message_service.service.kafka.KafkaProduceService;
import com.nqt.message_service.utils.formatter.DateFormatter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageServiceImp implements MessageService {

    @NonFinal
    String defaultSortProperty = "sentDate";

    MessageRepository messageRepository;

    ChatRoomService chatRoomService;

    KafkaProduceService kafkaProduceService;

    SimpMessagingTemplate simpMessagingTemplate;

    MessageMapper messageMapper;

    DateFormatter dateFormatter;

    @Override
    public MessageResponse saveMessage(SaveMessageRequest request) {
        String chatId = chatRoomService
                .getChatRoomId(request.getSenderId(), request.getRecipientId(), true)
                .orElseThrow(() -> new AppException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        Message message = buidMessage(request, chatId);

        messageRepository.save(message);

        String notificationBody =
                String.format("%s%s", request.getSenderName(), NotificationType.SAVE_MESSAGE.getBody());

        kafkaProduceService.sendNotification(
                NotificationType.SAVE_MESSAGE,
                List.of(Recipient.builder().userId(request.getRecipientId()).build()),
                message.getId(),
                notificationBody);

        MessageResponse messageResponse = buildMessageResponse(message);

        simpMessagingTemplate.convertAndSendToUser(request.getRecipientId(), "/queue/messages", messageResponse);
        simpMessagingTemplate.convertAndSendToUser(request.getSenderId(), "/queue/messages", messageResponse);
        return messageResponse;
    }

    @Override
    public PageResponse<MessageResponse> getMessages(String recipientId, int currentPage, int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Sort sort = Sort.by(Sort.Direction.DESC, defaultSortProperty);
        String chatId = chatRoomService
                .getChatRoomId(authentication.getName(), recipientId, true)
                .orElseThrow(() -> new AppException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);

        Page<Message> messagePages = messageRepository.findAllByChatId(chatId, pageable);

        List<MessageResponse> responses = messagePages.getContent().stream()
                .map(this::buildMessageResponse)
                .toList();

        return PageResponse.<MessageResponse>builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .totalElements(messagePages.getTotalElements())
                .totalPages(messagePages.getTotalPages())
                .data(responses)
                .build();
    }

    @Override
    public void markAsRead(String recipientId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chatId = chatRoomService
                .getChatRoomId(authentication.getName(), recipientId, false)
                .orElseThrow(() -> new AppException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        List<Message> unreadMessages = messageRepository.findAllByChatIdAndReadStatus(chatId, false);

        for (Message unreadMessage : unreadMessages) {
            unreadMessage.setReadStatus(true);
            messageRepository.save(unreadMessage);
        }
    }

    private MessageResponse buildMessageResponse(Message message) {
        MessageResponse messageResponse = messageMapper.toMessageResponse(message);
        messageResponse.setImages(encodeFile(message.getImages()));
        messageResponse.setAttachments(encodeFile(message.getAttachments()));
        messageResponse.setFormatedSentDate(dateFormatter.format(message.getSentDate()));

        return messageResponse;
    }

    private Message buidMessage(SaveMessageRequest request, String chatId) {
        Message message = Message.builder()
                .chatId(chatId)
                .senderId(request.getSenderId())
                .recipientId(request.getRecipientId())
                .readStatus(false)
                .content(request.getContent())
                .sentDate(new Date())
                .build();

        if (request.getImages() != null) {
            message.setImages(toBinaryList(request.getImages()));
        }

        if (request.getAttachments() != null) {
            message.setAttachments(toBinaryList(request.getAttachments()));
        }

        return message;
    }

    private List<Binary> toBinaryList(List<MultipartFile> multipartFiles) {
        List<Binary> binaryList = new ArrayList<>();
        try {
            for (MultipartFile file : multipartFiles) {
                binaryList.add(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
            }
        } catch (IOException e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        return binaryList;
    }

    private List<String> encodeFile(List<Binary> files) {
        if (files == null) {
            return List.of();
        }

        List<String> encodedFiles = new ArrayList<>();
        for (Binary file : files) {
            encodedFiles.add(Base64.getEncoder().encodeToString(file.getData()));
        }
        return encodedFiles;
    }
}
