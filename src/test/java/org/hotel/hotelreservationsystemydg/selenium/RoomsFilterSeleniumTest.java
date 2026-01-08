package org.hotel.hotelreservationsystemydg.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("selenium")
public class RoomsFilterSeleniumTest extends BaseSeleniumTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        if (isHeadless()) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void odaTipiFiltresiCalisir() {
        driver.get(getBaseUrl() + "/rooms");

        WebElement roomsTitle = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='rooms-title']")));
        assertTrue(roomsTitle.isDisplayed(), "Odalar sayfasi acilmali");

        WebElement typeFilter = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='filter-room-type']")));
        Select typeSelect = new Select(typeFilter);

        List<WebElement> options = typeSelect.getOptions();
        assertTrue(options.size() > 1, "En az bir oda tipi olmali");

        String targetValue = options.get(1).getAttribute("value");
        typeSelect.selectByValue(targetValue);

        String expectedLabel = mapRoomTypeLabel(targetValue);
        List<WebElement> roomTypeLabels = wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        By.cssSelector("[data-testid='room-type']"), 0));
        assertFalse(roomTypeLabels.isEmpty(), "Filtre sonrası oda kartı olmalı");

        for (WebElement label : roomTypeLabels) {
            assertTrue(label.getText().contains(expectedLabel),
                    "Filtrelenen odalar beklenen tipi içermeli");
        }
    }

    private String mapRoomTypeLabel(String typeValue) {
        String normalized = typeValue.toLowerCase();
        if (normalized.contains("single")) {
            return "Tek Kişilik";
        }
        if (normalized.contains("double")) {
            return "Çift Kişilik";
        }
        if (normalized.contains("standard")) {
            return "Standart";
        }
        if (normalized.contains("suite")) {
            return "Suit";
        }
        return typeValue;
    }
}
