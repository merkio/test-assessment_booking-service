package com.statista.code.challenge.api.booking.response;

import com.statista.code.challenge.domain.booking.Booking;
import lombok.Builder;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Builder
public class BookingResponse {

    public final String id;
    public final String description;
    public final BigDecimal price;
    public final Currency currency;
    public final LocalDateTime subscriptionStartDate;
    public final String email;
    public final String department;

    public static BookingResponse from(@NonNull Booking booking) {
        return BookingResponse.builder()
                .id(booking.id)
                .description(booking.description)
                .price(booking.price)
                .currency(booking.currency)
                .subscriptionStartDate(booking.subscriptionStartDate)
                .department(booking.department.name())
                .build();
    }
}
