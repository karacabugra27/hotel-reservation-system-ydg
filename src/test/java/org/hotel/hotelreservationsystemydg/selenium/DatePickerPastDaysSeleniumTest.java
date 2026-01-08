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

@Tag("selenium")
public class DatePickerPastDaysSeleniumTest extends BaseSeleniumTest {

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
    void gecmisTarihSecilemez() {
        driver.get(getBaseUrl() + "/rooms");

        WebElement detailButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("[data-testid^='room-detail-button-']")));
        detailButton.click();

        WebElement reserveButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("[data-testid='room-detail-reserve']")));
        reserveButton.click();

        LocalDate yesterday = LocalDate.now().minusDays(1);
        String dayValue = yesterday.format(DateTimeFormatter.ISO_LOCAL_DATE);
        WebElement dayButton = findDayButton(dayValue);

        assertFalse(dayButton.isEnabled(), "Gecmis tarih secilememeli");
    }

    private WebElement findDayButton(String isoDate) {
        List<WebElement> buttons = driver.findElements(
                By.cssSelector("[data-testid='day-" + isoDate + "']"));
        if (buttons.isEmpty()) {
            WebElement prevButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.cssSelector(".rdp-nav_button_previous")));
            prevButton.click();
            return wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("[data-testid='day-" + isoDate + "']")));
        }
        return buttons.get(0);
    }
}
