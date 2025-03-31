package com.statista.code.challenge.api.booking.request;

import com.statista.code.challenge.domain.currency.CurrencyUtils;
import com.statista.code.challenge.domain.booking.CreateBooking;
import com.statista.code.challenge.domain.department.DepartmentName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest {

    private String description;

    @NotNull
    private BigDecimal price;

    @NotBlank
    private String currency;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime subscriptionStartDate;

    @Email
    private String email;

    @NotNull
    private String department;

    public CreateBooking toCreateBooking() {
        return CreateBooking.builder()
                .description(description)
                .price(price)
                .currency(CurrencyUtils.from(currency))
                .department(DepartmentName.from(department))
                .email(email)
                .build();
    }
}
