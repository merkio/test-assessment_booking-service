package com.statista.code.challenge.repository.booking;

import com.statista.code.challenge.domain.booking.Booking;
import com.statista.code.challenge.domain.booking.BookingRepositoryAdapter;
import com.statista.code.challenge.domain.booking.CreateBooking;
import com.statista.code.challenge.domain.department.DepartmentName;
import com.statista.code.challenge.domain.exception.NotFoundEntityException;
import io.github.thibaultmeyer.cuid.CUID;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Currency;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class InMemoryBookingRepositoryAdapterImpl implements BookingRepositoryAdapter {

    private final Map<String, Booking> bookings = new ConcurrentHashMap<>();

    @Override
    public Booking create(CreateBooking createBooking) {
        var id = CUID.randomCUID2(12).toString();
        var createdBooking = createBooking.createBooking(id);
        bookings.put(id, createBooking.createBooking(id));
        return createdBooking;
    }

    @Override
    public Booking save(String bookingId, Booking booking) {
        bookings.put(bookingId, booking);
        return booking;
    }

    @Override
    public Booking get(String bookingId) {
        if (bookings.containsKey(bookingId)) {
            return bookings.get(bookingId);
        }
        throw new NotFoundEntityException(format("Booking [%s] doesn't exist", bookingId));
    }

    @Override
    public Collection<Booking> findAllByDepartment(DepartmentName department) {
        return bookings.values().stream()
                .filter(booking -> booking.department.equals(department))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Currency> findAllCurrencies() {
        return bookings.values().stream()
                .map(booking -> booking.currency)
                .collect(Collectors.toSet());
    }

    @Override
    public BigDecimal currencyPricesSum(Currency currency) {
        return bookings.values().stream()
                .filter(booking -> booking.currency.equals(currency))
                .map(booking -> booking.price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
