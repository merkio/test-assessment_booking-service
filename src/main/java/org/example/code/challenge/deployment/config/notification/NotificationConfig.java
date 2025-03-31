package org.example.code.challenge.deployment.config.notification;

import org.example.code.challenge.domain.notification.NoOpNotificationAdapterImpl;
import org.example.code.challenge.domain.notification.NotificationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfig {

    @Bean
    public NotificationAdapter notificationAdapter() {
        return new NoOpNotificationAdapterImpl();
    }
}
