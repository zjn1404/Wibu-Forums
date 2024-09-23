package com.nqt.message_service.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveMessageRequest {
    String senderId;
    String recipientId;
    String senderName;
    String content;
    List<MultipartFile> images;
    List<MultipartFile> attachments;
}
