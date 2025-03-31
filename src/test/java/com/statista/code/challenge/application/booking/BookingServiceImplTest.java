package com.statista.code.challenge.application.booking;

import com.statista.code.challenge.api.booking.request.CreateBookingRequest;
import com.statista.code.challenge.application.department.Department;
import com.statista.code.challenge.application.department.hotel.HotelDepartment;
import com.statista.code.challenge.domain.booking.Booking;
import com.statista.code.challenge.domain.booking.BookingRepositoryAdapter;
import com.statista.code.challenge.domain.booking.CreateBooking;
import com.statista.code.challenge.domain.department.DepartmentName;
import com.statista.code.challenge.domain.department.ResultStatus;
import com.statista.code.challenge.domain.notification.NotificationAdapter;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookingServiceImplTest {

    private final NotificationAdapter notificationAdapter = mock(NotificationAdapter.class);

    private final BookingRepositoryAdapter repository = mock(BookingRepositoryAdapter.class);

    private final BookingService bookingService = new BookingServiceImpl(
            new ArrayList<>(),
            repository,
            notificationAdapter
    );

    private final CreateBookingRequest createBookingRequest = CreateBookingRequest.builder()
            .description("test")
            .price(BigDecimal.TEN)
            .currency(EUR)
            .subscriptionStartDate(LocalDateTime.now())
            .email("user@example.com")
            .department(DepartmentName.HOTEL.name())
            .build();

    private final CreateBooking createBooking = createBookingRequest.toCreateBooking();

    private final Booking booking = createBooking.createBooking("bookingId");

    private static final Currency EUR = Currency.getInstance("EUR");
    private static final Currency USD = Currency.getInstance("USD");

    @Test
    void successfullyCreateNewBooking() {
        when(repository.create(createBookingRequest.toCreateBooking())).thenReturn(booking);

        var result = bookingService.create(createBooking);

        assertEquals(booking, result);
        verify(repository).create(createBooking);
    }

    @Test
    void successfullySaveBooking() {
        when(repository.save(booking.id, booking)).thenReturn(booking);
        var saved = bookingService.save(booking.id, booking);

        assertEquals(booking, saved);
        verify(repository).save(booking.id, booking);
    }

    @Test
    void successfullyGetBooking() {
        when(repository.get(booking.id)).thenReturn(booking);

        var result = bookingService.get(booking.id);

        assertEquals(booking, result);
        verify(repository).get(booking.id);
    }

    @Test
    void successfullyFindAllByDepartment() {
        var department = DepartmentName.HOTEL;
        when(repository.findAllByDepartment(department)).thenReturn(Collections.singletonList(booking));

        var response = bookingService.findAllByDepartment(department);

        assertEquals(Collections.singletonList(booking), response);
        verify(repository).findAllByDepartment(department);
    }

    @Test
    void successfullyFindAllCurrencies() {
        when(repository.findAllCurrencies()).thenReturn(Set.of(EUR, USD));

        var currencies = bookingService.findAllCurrencies();

        assertEquals(2, currencies.size());
        verify(repository).findAllCurrencies();
    }

    @Test
    void successfullyGetCurrencyPricesSum() {
        when(repository.currencyPricesSum(EUR)).thenReturn(BigDecimal.TEN);

        var sum = bookingService.currencyPricesSum(EUR);

        assertEquals(BigDecimal.TEN, sum);
        verify(repository).currencyPricesSum(EUR);
    }

    @Test
    void successfullyDoBusiness() {
        List<Department> listOfDepartments = Collections.singletonList(new HotelDepartment(notificationAdapter));
        var bookingService = new BookingServiceImpl(listOfDepartments, repository, notificationAdapter);
        when(repository.get(booking.id)).thenReturn(booking);
        doNothing().when(notificationAdapter).sendEmail(any());

        var result = bookingService.doBusiness(booking.id);

        assertEquals(ResultStatus.OK, result.status());
        verify(notificationAdapter).sendEmail(any());
    }

    // TODO Add negative scenarios and exception cases
}
