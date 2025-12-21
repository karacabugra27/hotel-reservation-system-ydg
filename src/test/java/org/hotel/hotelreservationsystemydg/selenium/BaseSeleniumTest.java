package org.hotel.hotelreservationsystemydg.selenium;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
@ActiveProfiles("selenium")
public class BaseSeleniumTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }
}
