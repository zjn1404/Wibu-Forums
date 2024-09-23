package com.nqt.message_service.service.chatroom;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nqt.message_service.entity.ChatRoom;
import com.nqt.message_service.repository.ChatRoomRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatRoomServiceImp implements ChatRoomService {

    ChatRoomRepository chatRoomRepository;

    @Override
    public Optional<String> getChatRoomId(String senderId, String recipientId, boolean createNewChatRoom) {
        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if (createNewChatRoom) {
                        String chatId = buildChatId(senderId, recipientId);
                        return Optional.of(chatId);
                    }

                    return Optional.empty();
                });
    }

    private String buildChatId(String senderId, String recipientId) {
        String chatId = String.format("%s-%s", senderId, recipientId);

        ChatRoom senderRecipient = ChatRoom.builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom recipientSender = ChatRoom.builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();

        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }
}
