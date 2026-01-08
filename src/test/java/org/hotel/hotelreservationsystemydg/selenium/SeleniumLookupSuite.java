package org.hotel.hotelreservationsystemydg.selenium;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        ReservationLookupValidSeleniumTest.class,
        ReservationLookupInvalidSeleniumTest.class
})
public class SeleniumLookupSuite {
}
