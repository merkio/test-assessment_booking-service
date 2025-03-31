package org.example.code.challenge.api.booking.request;

import org.example.code.challenge.domain.currency.CurrencyUtils;
import org.example.code.challenge.domain.booking.Booking;
import org.example.code.challenge.domain.department.DepartmentName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    private String description;

    @NotNull
    private BigDecimal price;

    @NotBlank
    private String currency;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime subscriptionStartDate;

    @Email
    private String email;

    @NotBlank
    private String department;

    public static BookingRequest from(@NotNull Booking booking) {
        return new BookingRequest(
                booking.description,
                booking.price,
                booking.currency.getCurrencyCode(),
                booking.subscriptionStartDate,
                booking.email,
                booking.department.name()
        );
    }

    public Booking toBooking(@NotNull String bookingId) {
        return Booking.builder()
                .id(bookingId)
                .description(description)
                .price(price)
                .currency(CurrencyUtils.from(currency))
                .subscriptionStartDate(subscriptionStartDate)
                .department(DepartmentName.from(department))
                .build();
    }
}
