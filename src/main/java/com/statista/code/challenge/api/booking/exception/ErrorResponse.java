package com.statista.code.challenge.api.booking.exception;

import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
public class ErrorResponse {

    public final String error;
    public final int code;
}
