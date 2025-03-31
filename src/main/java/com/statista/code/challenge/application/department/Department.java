package com.statista.code.challenge.application.department;

import com.statista.code.challenge.domain.booking.Booking;
import com.statista.code.challenge.domain.department.BusinessResult;
import com.statista.code.challenge.domain.department.DepartmentName;

public interface Department {

    DepartmentName name();

    BusinessResult doBusiness(Booking booking);
}
