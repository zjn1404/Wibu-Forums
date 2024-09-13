package com.nqt.event.notification;

import lombok.Getter;

@Getter
public enum NotificationType {
    ADD_FRIEND("FRIEND", "ADD FRIEND", " wants to be your friend"),
    ACCEPT_FRIEND("FRIEND", "ACCEPT FRIEND", " has accepted add friend request"),
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
