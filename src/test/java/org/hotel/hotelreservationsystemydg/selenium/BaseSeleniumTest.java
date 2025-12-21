package org.hotel.hotelreservationsystemydg.selenium;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseSeleniumTest {

    @BeforeAll
    static void setup() {
        String baseUrl = System.getenv("APP_BASE_URL");

        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = "http://localhost:8080";
        }

        RestAssured.baseURI = baseUrl;
    }
}