package org.example.code.challenge.domain.booking;

import org.example.code.challenge.domain.department.DepartmentName;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Objects;

@Builder
@EqualsAndHashCode
public class CreateBooking {
    public final String description;
    public final BigDecimal price;
    public final Currency currency;
    public final LocalDateTime subscriptionStartDate;
    public final String email;
    public final DepartmentName department;

    public CreateBooking build(CreateBookingBuilder builder) {
        Objects.requireNonNull(builder.price);
        Objects.requireNonNull(builder.currency);
        Objects.requireNonNull(builder.subscriptionStartDate);
        Objects.requireNonNull(builder.email);
        Objects.requireNonNull(builder.department);
        return new CreateBooking(
                builder.description,
                builder.price,
                builder.currency,
                builder.subscriptionStartDate,
                builder.email,
                builder.department
        );
    }

    public Booking createBooking(@NonNull String bookingId) {
        Objects.requireNonNull(bookingId);

        return new Booking(bookingId, description, price, currency, subscriptionStartDate, email, department);
    }
}
