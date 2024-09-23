package com.nqt.message_service.service.message;

import com.nqt.message_service.dto.request.SaveMessageRequest;
import com.nqt.message_service.dto.response.MessageResponse;
import com.nqt.message_service.dto.response.PageResponse;

public interface MessageService {
    MessageResponse saveMessage(SaveMessageRequest request);

    PageResponse<MessageResponse> getMessages(String recipientId, int currentPage, int pageSize);

    void markAsRead(String recipientId);
}
