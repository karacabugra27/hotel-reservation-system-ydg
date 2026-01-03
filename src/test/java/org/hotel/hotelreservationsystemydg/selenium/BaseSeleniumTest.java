package org.hotel.hotelreservationsystemydg.selenium;

public abstract class BaseSeleniumTest {

    protected String getBaseUrl() {
        String baseUrl = System.getenv("APP_BASE_URL");

        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = "http://localhost:8080";
        }

        return baseUrl;
    }

    protected boolean isHeadless() {
        String envValue = System.getenv("SELENIUM_HEADLESS");
        if (envValue != null && !envValue.isBlank()) {
            return Boolean.parseBoolean(envValue);
        }

        return Boolean.getBoolean("selenium.headless");
    }
}
