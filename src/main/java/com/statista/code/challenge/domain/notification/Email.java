package com.statista.code.challenge.domain.notification;

import lombok.Builder;

@Builder
public class Email<T> {
    public final String email;
    public final T body;
}
