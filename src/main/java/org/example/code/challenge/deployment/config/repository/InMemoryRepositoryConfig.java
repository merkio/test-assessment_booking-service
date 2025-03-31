package org.example.code.challenge.deployment.config.repository;

import org.example.code.challenge.domain.booking.BookingRepositoryAdapter;
import org.example.code.challenge.repository.booking.InMemoryBookingRepositoryAdapterImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InMemoryRepositoryConfig {

    @Bean
    public BookingRepositoryAdapter bookingRepositoryAdapter() {
        return new InMemoryBookingRepositoryAdapterImpl();
    }
}
