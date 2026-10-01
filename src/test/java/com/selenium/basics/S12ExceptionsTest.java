package com.selenium.basics;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** S12. The common Selenium exceptions and how they surface. */
class S12ExceptionsTest extends BaseTest {

    @Test
    @DisplayName("A missing locator raises NoSuchElementException")
    void noSuchElement() {
        driver.get(SITE_URL);
        // Turn off the 5s implicit wait from BaseTest so the lookup fails right away.
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        assertThrows(NoSuchElementException.class,
                () -> driver.findElement(By.id("definitely-not-present")));
    }

    @Test
    @DisplayName("A condition that never holds raises TimeoutException")
    void timeout() {
        driver.get(SITE_URL);
        // A short wait keeps the test fast. Explicit waits throw TimeoutException,
        // not NoSuchElementException, when the condition is still false at the end.
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        assertThrows(TimeoutException.class,
                () -> shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("never-appears"))));
    }

    @Test
    @DisplayName("Using a reference after the DOM re-renders raises StaleElementReferenceException")
    void staleElement() {
        driver.get(BASE + "/dynamic_content");
        WebElement stale = driver.findElement(By.cssSelector("#content"));
        // A reload builds a new DOM, so the old reference points to a node that no
        // longer exists. The fix is to find the element again.
        driver.navigate().refresh();
        assertThrows(StaleElementReferenceException.class, stale::getText);
    }
}
