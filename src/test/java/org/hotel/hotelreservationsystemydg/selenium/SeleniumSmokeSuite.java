package org.hotel.hotelreservationsystemydg.selenium;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
//deneme
@Suite
@SelectClasses({
        HomePageSeleniumTest.class,
        RoomsFilterSeleniumTest.class,
        RoomDetailSeleniumTest.class,
        ReservationNavigationSeleniumTest.class
})
public class SeleniumSmokeSuite {
}
