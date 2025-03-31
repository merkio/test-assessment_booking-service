package com.statista.code.challenge.domain.notification;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoOpNotificationAdapterImpl implements NotificationAdapter {

    @Override
    public <T> void sendEmail(Email<T> email) {
      log.debug("Ignore sending email to [{}]", email.email);
    }
}
