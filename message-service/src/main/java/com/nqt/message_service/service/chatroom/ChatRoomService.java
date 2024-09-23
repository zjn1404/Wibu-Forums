package com.nqt.message_service.service.chatroom;

import java.util.Optional;

public interface ChatRoomService {
    Optional<String> getChatRoomId(String senderId, String recipientId, boolean createNewChatRoom);
}
