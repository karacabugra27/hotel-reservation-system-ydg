package org.hotel.hotelreservationsystemydg.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
public class AvailableRoomsSeleniumTest extends BaseSeleniumTest {

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
    void musaitOdalarListelenebiliyor() {
        driver.get(getBaseUrl());

        WebElement homeButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("[data-testid='home-rooms-button']")));
        homeButton.click();

        WebElement roomsTitle = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='rooms-title']")));
        assertTrue(roomsTitle.isDisplayed(), "Odalar basligi gorunur olmali");

        WebElement statusFilter = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='filter-room-status']")));
        Select statusSelect = new Select(statusFilter);
        statusSelect.selectByValue("AVAILABLE");

        List<WebElement> detailButtons = wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        By.cssSelector("[data-testid^='room-detail-button-']"), 0));
        detailButtons.get(0).click();

        WebElement detailReserve = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("[data-testid='room-detail-reserve']")));
        detailReserve.click();

        WebElement reservationTitle = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='reservation-title']")));
        assertTrue(reservationTitle.isDisplayed(), "Rezervasyon sayfasi acilmali");

        driver.findElement(By.cssSelector("[data-testid='reservation-first-name']"))
                .sendKeys("Akademik");
        driver.findElement(By.cssSelector("[data-testid='reservation-last-name']"))
                .sendKeys("Test");
        driver.findElement(By.cssSelector("[data-testid='reservation-phone']"))
                .sendKeys("5551112233");

        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(2);
        String checkInValue = checkIn.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String checkOutValue = checkOut.format(DateTimeFormatter.ISO_LOCAL_DATE);

        driver.findElement(By.cssSelector("[data-testid='reservation-checkin']"))
                .sendKeys(checkInValue);
        driver.findElement(By.cssSelector("[data-testid='reservation-checkout']"))
                .sendKeys(checkOutValue);

        driver.findElement(By.cssSelector("[data-testid='reservation-submit']")).click();

        WebElement successAlert = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='reservation-success']")));
        assertTrue(successAlert.isDisplayed(), "Rezervasyon basarili olmali");

        WebElement reservationId = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='reservation-id']")));
        assertFalse(reservationId.getText().isBlank(), "Rezervasyon ID bos olmamali");

        WebElement summaryCard = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='reservation-summary']")));
        assertTrue(summaryCard.isDisplayed(), "Rezervasyon ozeti gorunmeli");
    }
}
