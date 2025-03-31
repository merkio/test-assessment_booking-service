package org.example.code.challenge.repository.booking;

import org.example.code.challenge.api.booking.request.CreateBookingRequest;
import org.example.code.challenge.domain.booking.Booking;
import org.example.code.challenge.domain.booking.CreateBooking;
import org.example.code.challenge.domain.department.DepartmentName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


class InMemoryBookingRepositoryAdapterImplTest {

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

    private InMemoryBookingRepositoryAdapterImpl repositoryAdapter;

    @BeforeEach
    void setUp() {
        repositoryAdapter = new InMemoryBookingRepositoryAdapterImpl();
    }

    @Test
    void successfullyCreateBooking() {
        var createdBooking = repositoryAdapter.create(createBooking);

        assertNotNull(createdBooking.id);
        assertEquals(createBooking.description, createdBooking.description);
        assertEquals(createBooking.price, createdBooking.price);
        assertEquals(createBooking.currency, createdBooking.currency);
        assertEquals(createBooking.department, createdBooking.department);
    }

    @Test
    void successfullySaveBooking() {
        var savedBooking = repositoryAdapter.save(booking.id, booking);

        assertEquals(booking, savedBooking);
    }

    @Test
    void successfullyGetBooking() {
        repositoryAdapter.save(booking.id, booking);
        var savedBooking = repositoryAdapter.get(booking.id);

        assertEquals(booking, savedBooking);
    }

    @Test
    void findAllByDepartment() {
        var carBookings = IntStream.range(0, 2)
                .mapToObj(idx -> repositoryAdapter.create(createBookingWithDepartment(DepartmentName.CAR)))
                .collect(Collectors.toSet());
        repositoryAdapter.create(createBookingWithDepartment(DepartmentName.HOTEL));

        var foundCarBookings = new HashSet<>(repositoryAdapter.findAllByDepartment(DepartmentName.CAR));
        assertEquals(carBookings, foundCarBookings);
    }

    @Test
    void findAllCurrencies() {
        repositoryAdapter.create(createBookingWithCurrency(EUR));
        repositoryAdapter.create(createBookingWithCurrency(USD));

        var foundCurrencies = repositoryAdapter.findAllCurrencies();

        assertEquals(2, foundCurrencies.size());
        assertEquals(Set.of(EUR, USD), foundCurrencies);
    }

    @Test
    void getCurrencyPricesSum() {
        IntStream.range(0, 2).forEach(idx -> repositoryAdapter.create(createBooking));
        var expectedPriceSum = createBooking.price.multiply(BigDecimal.valueOf(2));

        var sum = repositoryAdapter.currencyPricesSum(createBooking.currency);

        assertEquals(expectedPriceSum, sum);
    }

    // TODO add negative and corner cases

    private CreateBooking createBookingWithDepartment(DepartmentName department) {
        return CreateBooking.builder()
                .description(createBooking.description)
                .price(createBooking.price)
                .currency(createBooking.currency)
                .department(department)
                .email(createBooking.email)
                .build();
    }


    private CreateBooking createBookingWithCurrency(Currency currency) {
        return CreateBooking.builder()
                .description(createBooking.description)
                .price(createBooking.price)
                .currency(currency)
                .department(createBooking.department)
                .email(createBooking.email)
                .build();
    }
}