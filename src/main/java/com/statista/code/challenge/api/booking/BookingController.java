package com.statista.code.challenge.api.booking;

import com.statista.code.challenge.api.booking.request.CreateBookingRequest;
import com.statista.code.challenge.api.booking.request.BookingRequest;
import com.statista.code.challenge.api.booking.response.*;
import com.statista.code.challenge.api.booking.response.business.DoBusinessResponse;
import com.statista.code.challenge.api.booking.response.currency.CurrenciesResponse;
import com.statista.code.challenge.api.booking.response.currency.CurrencySumResponse;
import com.statista.code.challenge.application.booking.BookingService;
import com.statista.code.challenge.domain.department.DepartmentName;
import com.statista.code.challenge.domain.exception.NotFoundEntityException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Currency;

import static java.lang.String.format;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookingservice/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> create(@Valid @RequestBody CreateBookingRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BookingResponse.from(bookingService.create(request.toCreateBooking())));
    }

    @PutMapping("/{booking_id}")
    public BookingResponse save(@PathVariable("booking_id") String bookingId, @Valid @RequestBody BookingRequest request) {
        return BookingResponse.from(bookingService.save(bookingId, request.toBooking(bookingId)));
    }

    @GetMapping("/{booking_id}")
    public BookingResponse get(@PathVariable("booking_id") String bookingId) {
        return BookingResponse.from(bookingService.get(bookingId));
    }

    @GetMapping("/department/{department}")
    public DepartmentBookingsResponse findAllByDepartment(@PathVariable("department") String department) {
        return DepartmentBookingsResponse.from(bookingService.findAllByDepartment(DepartmentName.valueOf(department)));
    }

    @GetMapping("/currencies")
    public CurrenciesResponse findAllCurrencies() {
        return new CurrenciesResponse(bookingService.findAllCurrencies());
    }

    @GetMapping("/sum/{currency}")
    public CurrencySumResponse getCurrencyPricesSum(@PathVariable("currency") String currencyCode) {
        var currency = Currency.getAvailableCurrencies().stream()
                .filter(it -> it.getCurrencyCode().equalsIgnoreCase(currencyCode))
                .findFirst()
                .orElseThrow(
                        () -> new NotFoundEntityException(
                                format("Currency with currency code [%s] not found", currencyCode)
                        )
                );
        return new CurrencySumResponse(bookingService.currencyPricesSum(currency));
    }

    @GetMapping("/dobusiness/{booking_id}")
    public DoBusinessResponse doBusiness(@PathVariable("booking_id") String bookingId) {
        return new DoBusinessResponse(bookingService.doBusiness(bookingId));
    }
}
