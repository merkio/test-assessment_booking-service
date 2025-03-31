package com.statista.code.challenge.application.booking;

import com.statista.code.challenge.domain.booking.Booking;
import com.statista.code.challenge.domain.booking.CreateBooking;
import com.statista.code.challenge.domain.department.BusinessResult;
import com.statista.code.challenge.domain.department.DepartmentName;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Currency;
import java.util.Set;

public interface BookingService {

    Booking create(CreateBooking createCommand);

    Booking save(String bookingId, Booking booking);

    Booking get(String bookingId);

    Collection<Booking> findAllByDepartment(DepartmentName department);

    Set<Currency> findAllCurrencies();

    BigDecimal currencyPricesSum(Currency currency);

    BusinessResult doBusiness(String bookingId);
}
