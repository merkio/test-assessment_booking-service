package org.example.code.challenge.domain.currency;

import org.example.code.challenge.domain.exception.NotFoundEntityException;

import java.util.Currency;

import static java.lang.String.format;

public class CurrencyUtils {

    public static Currency from(String currencyCode) {
        try {
            return Currency.getInstance(currencyCode);
        } catch (IllegalArgumentException e) {
            throw new NotFoundEntityException(format("Currency code [%s] not found", currencyCode));
        }
    }
}
