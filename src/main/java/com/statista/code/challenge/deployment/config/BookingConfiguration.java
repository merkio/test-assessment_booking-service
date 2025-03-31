package com.statista.code.challenge.deployment.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.statista.code.challenge.api.booking.BookingController;
import com.statista.code.challenge.api.booking.exception.BookingControllerExceptionHandler;
import com.statista.code.challenge.application.booking.BookingService;
import com.statista.code.challenge.application.booking.BookingServiceImpl;
import com.statista.code.challenge.application.department.Department;
import com.statista.code.challenge.domain.booking.BookingRepositoryAdapter;
import com.statista.code.challenge.domain.notification.NotificationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
public class BookingConfiguration {

    @Bean
    public BookingControllerExceptionHandler bookingControllerExceptionHandler() {
        return new BookingControllerExceptionHandler();
    }

    @Bean
    public BookingService bookingService(
            List<Department> departments,
            BookingRepositoryAdapter bookingRepositoryAdapter,
            NotificationAdapter notificationAdapter
    ) {
        return new BookingServiceImpl(departments, bookingRepositoryAdapter, notificationAdapter);
    }

    @Bean
    public BookingController bookingController(BookingService bookingService) {
        return new BookingController(bookingService);
    }

    @Bean
    public ObjectMapper objectMapper() {
        var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(
                        new JavaTimeModule()
                                .addSerializer(new LocalDateTimeSerializer(dateTimeFormatter))
                                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter))
                );
    }
}
