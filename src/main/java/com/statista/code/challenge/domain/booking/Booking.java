package com.statista.code.challenge.domain.booking;

import com.statista.code.challenge.domain.department.DepartmentName;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Objects;

@Builder
@EqualsAndHashCode
public class Booking {

    public final String id;
    public final String description;
    public final BigDecimal price;
    public final Currency currency;
    public final LocalDateTime subscriptionStartDate;
    public final String email;
    public final DepartmentName department;

    public static Booking build(@NotNull BookingBuilder builder) {
        Objects.requireNonNull(builder.id);
        Objects.requireNonNull(builder.price);
        Objects.requireNonNull(builder.currency);
        Objects.requireNonNull(builder.subscriptionStartDate);
        Objects.requireNonNull(builder.email);
        Objects.requireNonNull(builder.department);
        return new Booking(
                builder.id,
                builder.description,
                builder.price,
                builder.currency,
                builder.subscriptionStartDate,
                builder.email,
                builder.department
        );
    }
}
