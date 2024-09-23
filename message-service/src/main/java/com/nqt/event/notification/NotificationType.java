package com.nqt.event.notification;

import lombok.Getter;

@Getter
public enum NotificationType {
    SAVE_MESSAGE("MESSAGE", "SAVE MESSAGE", " has sent a new message"),
    ;
    final String channel;
    final String subject;
    final String body;

    NotificationType(String channel, String subject, String body) {
        this.channel = channel;
        this.subject = subject;
        this.body = body;
    }
}
