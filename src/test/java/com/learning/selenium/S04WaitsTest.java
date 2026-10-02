package com.learning.selenium;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

/** S4. Synchronization with explicit and fluent waits. */
class S04WaitsTest extends BaseTest {

    @Test
    @DisplayName("Explicit wait observes a checkbox being removed")
    void explicitWaitRemovesCheckbox() {
        driver.get(BASE + "/dynamic_controls");
        driver.findElement(By.cssSelector("#checkbox-example button")).click();
        // The checkbox is removed after an AJAX call; invisibility* also passes once it
        // is gone from the DOM entirely.
        assertTrue(wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("checkbox"))));
    }

    @Test
    @DisplayName("Explicit wait waits for a disabled input to become editable")
    void explicitWaitEnablesInput() {
        driver.get(BASE + "/dynamic_controls");
        driver.findElement(By.cssSelector("#input-example button")).click();
        // elementToBeClickable = visible and enabled. It returns the element once true.
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#input-example input")));
        input.sendKeys("enabled now");
        assertEquals("enabled now", input.getDomProperty("value"));
    }

    @Test
    @DisplayName("FluentWait polls until dynamic loading completes")
    void fluentWaitDynamicLoading() {
        driver.get(BASE + "/dynamic_loading/1");
        driver.findElement(By.cssSelector("#start button")).click();
        // FluentWait lets you choose the timeout, how often to poll, and which
        // exceptions count as "not ready yet" instead of failing the wait.
        // It must be Selenium's NoSuchElementException, not java.util's.
        Wait<WebDriver> fluent = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(250))
                .ignoring(NoSuchElementException.class);
        // #finish is in the DOM from the start but hidden, so wait for visibility.
        WebElement finish = fluent.until(ExpectedConditions.visibilityOfElementLocated(By.id("finish")));
        assertTrue(finish.getText().contains("Hello World"));
    }
}
