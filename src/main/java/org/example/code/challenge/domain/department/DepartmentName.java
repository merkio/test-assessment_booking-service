package org.example.code.challenge.domain.department;

import org.example.code.challenge.domain.exception.NotFoundEntityException;

import java.util.Arrays;

import static java.lang.String.format;

public enum DepartmentName {
    HOTEL,
    CAR;

    public static DepartmentName from(String name) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new NotFoundEntityException(format("Department name [%s] not found", name)));
    }
}
