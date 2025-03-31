package com.statista.code.challenge.domain.booking;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Currency;

@Builder
public class BookingEmailDetails {
    public final String bookingId;
    public final String description;
    public final BigDecimal price;
    public final Currency currency;
}
