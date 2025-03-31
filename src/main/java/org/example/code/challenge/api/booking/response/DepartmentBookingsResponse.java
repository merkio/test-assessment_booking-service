package org.example.code.challenge.api.booking.response;

import org.example.code.challenge.domain.booking.Booking;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public record DepartmentBookingsResponse(Set<String> bookings) {

    public static DepartmentBookingsResponse from(Collection<Booking> bookings) {
        return new DepartmentBookingsResponse(
                bookings.stream()
                        .map(booking -> booking.id)
                        .collect(Collectors.toSet())
        );
    }
}
