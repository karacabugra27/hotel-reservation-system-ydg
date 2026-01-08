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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("selenium")
public class ReservationLookupValidSeleniumTest extends BaseSeleniumTest {

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
    void rezervasyonKoduIleSorguBasarili() {
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
                .sendKeys("Lookup");
        driver.findElement(By.cssSelector("[data-testid='reservation-last-name']"))
                .sendKeys("Test");
        driver.findElement(By.cssSelector("[data-testid='reservation-phone']"))
                .sendKeys("5559998877");

        LocalDate checkIn = LocalDate.now().plusDays(5);
        LocalDate checkOut = LocalDate.now().plusDays(6);
        clickDay(checkIn);
        clickDay(checkOut);

        driver.findElement(By.cssSelector("[data-testid='reservation-submit']")).click();

        WebElement reservationCode = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='reservation-code']")));
        String reservationCodeValue = reservationCode.getText().replaceAll("\\s", "");
        assertFalse(reservationCodeValue.isBlank(), "Rezervasyon kodu gelmeli");

        WebElement lookupButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("[data-testid='go-to-reservation-lookup']")));
        lookupButton.click();

        WebElement lookupInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='reservation-lookup-input']")));
        lookupInput.sendKeys(reservationCodeValue);

        driver.findElement(By.cssSelector("[data-testid='reservation-lookup-submit']")).click();

        WebElement summaryCard = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='reservation-summary']")));
        assertTrue(summaryCard.isDisplayed(), "Rezervasyon ozeti gorunmeli");
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
}
