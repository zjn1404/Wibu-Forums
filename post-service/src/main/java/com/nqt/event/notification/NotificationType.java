package com.nqt.event.notification;

import lombok.Getter;

@Getter
public enum NotificationType {
    CREATE_POST("POST", "CREATE POST", "A new post has been created by "),
    CREATE_COMMENT("COMMENT", "CREATE COMMENT", " has commented on your post");

    final String channel;
    final String subject;
    final String body;

    NotificationType(String channel, String subject, String body) {
        this.channel = channel;
        this.subject = subject;
        this.body = body;
    }
}
