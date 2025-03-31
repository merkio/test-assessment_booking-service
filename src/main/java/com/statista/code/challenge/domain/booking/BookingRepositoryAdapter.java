package com.statista.code.challenge.domain.booking;

import com.statista.code.challenge.domain.department.DepartmentName;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Currency;
import java.util.Set;

public interface BookingRepositoryAdapter {

    Booking create(CreateBooking createBooking);

    Booking save(String bookingId, Booking booking);

    Booking get(String bookingId);

    Collection<Booking> findAllByDepartment(DepartmentName department);

    Set<Currency> findAllCurrencies();

    BigDecimal currencyPricesSum(Currency currency);
}
