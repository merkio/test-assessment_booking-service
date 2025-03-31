package com.statista.code.challenge.application.booking;

import com.statista.code.challenge.application.department.Department;
import com.statista.code.challenge.domain.booking.Booking;
import com.statista.code.challenge.domain.booking.BookingEmailDetails;
import com.statista.code.challenge.domain.booking.BookingRepositoryAdapter;
import com.statista.code.challenge.domain.booking.CreateBooking;
import com.statista.code.challenge.domain.department.BusinessResult;
import com.statista.code.challenge.domain.department.DepartmentName;
import com.statista.code.challenge.domain.department.ResultStatus;
import com.statista.code.challenge.domain.notification.Email;
import com.statista.code.challenge.domain.notification.NotificationAdapter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Currency;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class BookingServiceImpl implements BookingService {

    private final Map<DepartmentName, Department> departmentMap;
    private final BookingRepositoryAdapter bookingRepositoryAdapter;
    private final NotificationAdapter notificationAdapter;

    public BookingServiceImpl(
            Collection<Department> departments,
            BookingRepositoryAdapter bookingRepositoryAdapter,
            NotificationAdapter notificationAdapter
    ) {
        departmentMap = departments.stream().collect(Collectors.toMap(Department::name, it -> it));
        this.bookingRepositoryAdapter = bookingRepositoryAdapter;
        this.notificationAdapter = notificationAdapter;
    }

    @Override
    public Booking create(CreateBooking createCommand) {
        var booking = bookingRepositoryAdapter.create(createCommand);
        notificationAdapter.sendEmail(
                Email.builder()
                        .email(booking.email)
                        .body(BookingEmailDetails.builder()
                                .bookingId(booking.id)
                                .price(booking.price)
                                .description(booking.description)
                                .currency(booking.currency)
                                .build()
                        )
                        .build()
        );
        return booking;
    }

    @Override
    public Booking save(String bookingId, Booking booking) {
        return bookingRepositoryAdapter.save(bookingId, booking);
    }

    @Override
    public Booking get(String bookingId) {
        return bookingRepositoryAdapter.get(bookingId);
    }

    @Override
    public Collection<Booking> findAllByDepartment(DepartmentName department) {
        return bookingRepositoryAdapter.findAllByDepartment(department);
    }

    @Override
    public Set<Currency> findAllCurrencies() {
        return bookingRepositoryAdapter.findAllCurrencies();
    }

    @Override
    public BigDecimal currencyPricesSum(Currency currency) {
        return bookingRepositoryAdapter.currencyPricesSum(currency);
    }

    @Override
    public BusinessResult doBusiness(String bookingId) {
        var booking = bookingRepositoryAdapter.get(bookingId);
        if (departmentMap.containsKey(booking.department)) {
            return departmentMap.get(booking.department).doBusiness(booking);
        }
        return new BusinessResult(ResultStatus.ERROR);
    }
}
