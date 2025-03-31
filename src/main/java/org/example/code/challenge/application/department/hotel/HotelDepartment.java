package org.example.code.challenge.application.department.hotel;

import org.example.code.challenge.application.department.Department;
import org.example.code.challenge.domain.booking.Booking;
import org.example.code.challenge.domain.department.BusinessResult;
import org.example.code.challenge.domain.department.DepartmentName;
import org.example.code.challenge.domain.department.ResultStatus;
import org.example.code.challenge.domain.notification.Email;
import org.example.code.challenge.domain.notification.NotificationAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class HotelDepartment implements Department {

    private final NotificationAdapter notificationAdapter;

    @Override
    public DepartmentName name() {
        return DepartmentName.HOTEL;
    }

    @Override
    public BusinessResult doBusiness(Booking booking) {
        try {
            // do some business
            notificationAdapter.sendEmail(Email.builder().email(booking.email).body(booking.id).build());
            return new BusinessResult(ResultStatus.OK);
        } catch (Exception e) {
            log.error("Error during doBusiness with booking [{}]", booking.id);
            return new BusinessResult(ResultStatus.ERROR);
        }
    }
}
