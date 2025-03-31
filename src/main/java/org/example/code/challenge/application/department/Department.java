package org.example.code.challenge.application.department;

import org.example.code.challenge.domain.booking.Booking;
import org.example.code.challenge.domain.department.BusinessResult;
import org.example.code.challenge.domain.department.DepartmentName;

public interface Department {

    DepartmentName name();

    BusinessResult doBusiness(Booking booking);
}
