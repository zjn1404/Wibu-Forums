package com.nqt.message_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import com.nqt.message_service.dto.request.SaveMessageRequest;
import com.nqt.message_service.dto.response.ApiResponse;
import com.nqt.message_service.dto.response.MessageResponse;
import com.nqt.message_service.dto.response.PageResponse;
import com.nqt.message_service.service.message.MessageService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageController {
    MessageService messageService;

    @NonFinal
    @Value("${message.controller.message.mark-as-read-success}")
    String markAsReadSuccessMessage;

    @MessageMapping("/chat")
    public ApiResponse<MessageResponse> processChatMessage(@Payload SaveMessageRequest request) {
        return ApiResponse.<MessageResponse>builder()
                .result(messageService.saveMessage(request))
                .build();
    }

    @GetMapping("/get-all-messages")
    public ApiResponse<PageResponse<MessageResponse>> getAllMessages(
            @RequestParam(value = "page", required = false, defaultValue = "1") int currentPage,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam("recipientId") String recipientId) {
        return ApiResponse.<PageResponse<MessageResponse>>builder()
                .result(messageService.getMessages(recipientId, currentPage, size))
                .build();
    }

    @PutMapping("/mark-as-read")
    public ApiResponse<Void> markAsRead(@RequestParam("recipientId") String recipientId) {
        messageService.markAsRead(recipientId);

        return ApiResponse.<Void>builder().message(markAsReadSuccessMessage).build();
    }
}
