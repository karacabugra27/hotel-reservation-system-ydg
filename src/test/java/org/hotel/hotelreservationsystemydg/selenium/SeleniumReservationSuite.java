package org.hotel.hotelreservationsystemydg.selenium;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        AvailableRoomsSeleniumTest.class,
        DatePickerPastDaysSeleniumTest.class,
        ReservationDisabledDatesSeleniumTest.class,
        ReservationCodeCopySeleniumTest.class
})
public class SeleniumReservationSuite {
}
