package com.statista.code.challenge.deployment.config.repository;

import com.statista.code.challenge.domain.booking.BookingRepositoryAdapter;
import com.statista.code.challenge.repository.booking.InMemoryBookingRepositoryAdapterImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InMemoryRepositoryConfig {

    @Bean
    public BookingRepositoryAdapter bookingRepositoryAdapter() {
        return new InMemoryBookingRepositoryAdapterImpl();
    }
}
