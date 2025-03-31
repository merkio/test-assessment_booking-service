package com.statista.code.challenge.domain.notification;

public interface NotificationAdapter {

    <T> void sendEmail(Email<T> email);
}
