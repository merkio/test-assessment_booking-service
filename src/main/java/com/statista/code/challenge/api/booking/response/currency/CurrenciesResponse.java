package com.statista.code.challenge.api.booking.response.currency;

import java.util.Currency;
import java.util.Set;

public record CurrenciesResponse(Set<Currency> currencies) {
}
