package com.statista.code.challenge.api.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.statista.code.challenge.api.booking.request.BookingRequest;
import com.statista.code.challenge.api.booking.request.CreateBookingRequest;
import com.statista.code.challenge.application.booking.BookingService;
import com.statista.code.challenge.deployment.BookingServiceApplication;
import com.statista.code.challenge.domain.booking.Booking;
import com.statista.code.challenge.domain.department.DepartmentName;
import com.statista.code.challenge.domain.exception.NotFoundEntityException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Set;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = BookingServiceApplication.class)
class BookingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private final CreateBookingRequest createBookingRequest = CreateBookingRequest.builder()
            .description("test")
            .price(BigDecimal.valueOf(2))
            .currency(Currency.getInstance("EUR"))
            .subscriptionStartDate(LocalDateTime.now())
            .email("user@example.com")
            .department(DepartmentName.HOTEL.name())
            .build();

    private final Booking booking = Booking.builder()
            .id("testId")
            .price(createBookingRequest.getPrice())
            .currency(createBookingRequest.getCurrency())
            .description(createBookingRequest.getDescription())
            .email(createBookingRequest.getEmail())
            .subscriptionStartDate(createBookingRequest.getSubscriptionStartDate())
            .department(DepartmentName.HOTEL)
            .build();

    private static final Currency EUR = Currency.getInstance("EUR");
    private static final Currency USD = Currency.getInstance("USD");


    @Test
    void createBooking() throws Exception {
        when(bookingService.create(createBookingRequest.toCreateBooking())).thenReturn(booking);

        mockMvc.perform(post("/bookingservice/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookingRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(booking.id))
                .andReturn();

        verify(bookingService).create(any());
    }

    @Test
    void createInvalidCurrency() throws Exception {
        var createBooking = CreateBookingRequest.builder()
                .description("test")
                .price(BigDecimal.TEN)
                .currency(Currency.getInstance("EUR")) // Valid currency
                .subscriptionStartDate(LocalDateTime.now())
                .email("user@example.com")
                .department(DepartmentName.HOTEL.name())
                .build();

        doThrow(new IllegalArgumentException("Invalid currency")).when(bookingService).create(createBooking.toCreateBooking());

        mockMvc.perform(post("/bookingservice/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBooking))
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(bookingService).create(any());
    }

    @Test
    void returnNotFoundStatusCodeIfDepartmentWasNotFound() throws Exception {
        doThrow(new NotFoundEntityException()).when(bookingService).create(createBookingRequest.toCreateBooking());

        mockMvc.perform(post("/bookingservice/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookingRequest))
                )
                .andExpect(status().isNotFound())
                .andReturn();

        verify(bookingService).create(createBookingRequest.toCreateBooking());
    }

    @Test
    void saveBooking() throws Exception {
        when(bookingService.save(any(), any())).thenReturn(booking);

        mockMvc.perform(put("/bookingservice/bookings/" + booking.id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BookingRequest.from(booking)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.id))
                .andReturn();

        verify(bookingService).save(eq(booking.id), any());
    }

    @Test
    void getBookingById() throws Exception {
        when(bookingService.get(booking.id)).thenReturn(booking);

        mockMvc.perform(get("/bookingservice/bookings/" + booking.id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.id))
                .andReturn();

        verify(bookingService).get(booking.id);
    }

    @Test
    void findAllByDepartment() throws Exception {
        var listOfBookings = IntStream.range(0, 5)
                .mapToObj(idx -> createBookingWithIdAndDepartment(String.valueOf(idx), DepartmentName.CAR))
                .toList();
        when(bookingService.findAllByDepartment(DepartmentName.CAR)).thenReturn(listOfBookings);

        mockMvc.perform(get("/bookingservice/bookings/department/" + DepartmentName.CAR.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookings").exists())
                .andExpect(jsonPath("$.bookings").isArray())
                .andExpect(jsonPath("$.bookings", hasSize(5)))
                .andReturn();

        verify(bookingService).findAllByDepartment(DepartmentName.CAR);
    }

    @Test
    void findAllCurrencies() throws Exception {
        var expectedListOfCurrencies = Set.of(EUR, USD);
        when(bookingService.findAllCurrencies()).thenReturn(expectedListOfCurrencies);

        mockMvc.perform(get("/bookingservice/bookings/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencies").exists())
                .andExpect(jsonPath("$.currencies").isArray())
                .andExpect(jsonPath("$.currencies", hasSize(2)))
                .andReturn();

        verify(bookingService).findAllCurrencies();
    }

    @Test
    void getPriceSumForCurrency() throws Exception {
        var expectedSum = BigDecimal.valueOf(2.5);
        when(bookingService.currencyPricesSum(EUR)).thenReturn(expectedSum);

        mockMvc.perform(get("/bookingservice/bookings/sum/EUR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").exists())
                .andExpect(jsonPath("$.sum").value(expectedSum))
                .andReturn();

        verify(bookingService).currencyPricesSum(EUR);
    }

    private Booking createBookingWithIdAndDepartment(String id, DepartmentName department) {
        return Booking.builder()
                .id(id)
                .description("Test booking " + id)
                .price(BigDecimal.ONE)
                .currency(Currency.getInstance("EUR"))
                .subscriptionStartDate(LocalDateTime.now())
                .email("user" + id + "@example.com")
                .department(department)
                .build();
    }

    // TODO add more negative and corner cases
}
