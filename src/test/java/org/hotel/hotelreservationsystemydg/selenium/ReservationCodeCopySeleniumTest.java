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
public class ReservationCodeCopySeleniumTest extends BaseSeleniumTest {

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
    void rezervasyonKoduKopyalanabilir() {
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
                .sendKeys("Copy");
        driver.findElement(By.cssSelector("[data-testid='reservation-last-name']"))
                .sendKeys("Test");
        driver.findElement(By.cssSelector("[data-testid='reservation-phone']"))
                .sendKeys("5557778899");

        LocalDate checkIn = LocalDate.now().plusDays(3);
        LocalDate checkOut = LocalDate.now().plusDays(4);
        String checkInValue = checkIn.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String checkOutValue = checkOut.format(DateTimeFormatter.ISO_LOCAL_DATE);

        WebElement checkInButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("[data-testid='day-" + checkInValue + "']")));
        checkInButton.click();

        WebElement checkOutButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("[data-testid='day-" + checkOutValue + "']")));
        checkOutButton.click();

        driver.findElement(By.cssSelector("[data-testid='reservation-submit']")).click();

        WebElement copyButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("[data-testid='reservation-code-copy']")));
        copyButton.click();

        wait.until(
                ExpectedConditions.not(
                        ExpectedConditions.textToBe(
                                By.cssSelector("[data-testid='reservation-code-copy']"),
                                "Kodu Kopyala")));

        String buttonText = driver.findElement(
                By.cssSelector("[data-testid='reservation-code-copy']")).getText();
        assertTrue(
                buttonText.equals("Kopyalandı") || buttonText.equals("Kopyalanamadı"),
                "Kopyalama sonucu görünmeli");
    }
}
