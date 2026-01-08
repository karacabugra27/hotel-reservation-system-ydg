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
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("selenium")
public class ReservationDisabledDatesSeleniumTest extends BaseSeleniumTest {

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
    void doluTarihSecilemez() {
        driver.get(getBaseUrl() + "/rooms");

        List<WebElement> detailButtons = wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        By.cssSelector("[data-testid^='room-detail-button-']"), 0));
        detailButtons.get(0).click();

        WebElement reserveButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("[data-testid='room-detail-reserve']")));
        reserveButton.click();

        driver.findElement(By.cssSelector("[data-testid='reservation-first-name']"))
                .sendKeys("Booked");
        driver.findElement(By.cssSelector("[data-testid='reservation-last-name']"))
                .sendKeys("Range");
        driver.findElement(By.cssSelector("[data-testid='reservation-phone']"))
                .sendKeys("5556667788");

        LocalDate checkIn = LocalDate.now().plusDays(7);
        LocalDate checkOut = LocalDate.now().plusDays(8);
        clickDay(checkIn);
        clickDay(checkOut);

        driver.findElement(By.cssSelector("[data-testid='reservation-submit']")).click();

        WebElement backToRooms = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("[data-testid='back-to-rooms']")));
        backToRooms.click();

        List<WebElement> returnDetailButtons = wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        By.cssSelector("[data-testid^='room-detail-button-']"), 0));
        returnDetailButtons.get(0).click();

        WebElement returnReserveButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("[data-testid='room-detail-reserve']")));
        returnReserveButton.click();

        String checkInValue = checkIn.format(DateTimeFormatter.ISO_LOCAL_DATE);
        WebElement blockedButton = findDayButton(checkInValue);
        String ariaDisabled = blockedButton.getAttribute("aria-disabled");
        String disabledAttr = blockedButton.getAttribute("disabled");
        assertTrue("true".equals(ariaDisabled) || disabledAttr != null, "Dolu tarih disabled olmali");
    }

    private void clickDay(LocalDate date) {
        String dayValue = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        List<WebElement> buttons = driver.findElements(
                By.cssSelector("[data-testid='day-" + dayValue + "']"));
        if (buttons.isEmpty()) {
            WebElement nextButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.cssSelector(".rdp-nav_button_next")));
            nextButton.click();
        }
        WebElement dayButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("[data-testid='day-" + dayValue + "']")));
        dayButton.click();
    }

    private WebElement findDayButton(String isoDate) {
        List<WebElement> buttons = driver.findElements(
                By.cssSelector("[data-testid='day-" + isoDate + "']"));
        if (buttons.isEmpty()) {
            WebElement nextButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.cssSelector(".rdp-nav_button_next")));
            nextButton.click();
            return wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("[data-testid='day-" + isoDate + "']")));
        }
        return buttons.get(0);
    }
}
