package org.hotel.hotelreservationsystemydg.selenium;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AvailableRoomsSeleniumTest extends BaseSeleniumTest {

    @Test
    void musaitOdalarListelenebiliyor() {

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/room/available")
                .then()
                .statusCode(200)
                .body("$", is(not(empty())))
                .body("[0].roomNumber", notNullValue())
                .body("[0].status", equalTo("AVAILABLE"))
                .body("[0].roomTypeName", notNullValue());
    }
}
