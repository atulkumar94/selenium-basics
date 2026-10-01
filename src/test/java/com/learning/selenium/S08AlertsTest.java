package com.learning.selenium;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * S8. JavaScript dialogs: alert, confirm and prompt. Dialogs are not part of the
 * DOM, so they are handled through switchTo().alert() (here via alertIsPresent).
 * While a dialog is open, any other driver call throws UnhandledAlertException.
 */
class S08AlertsTest extends BaseTest {

    @Test
    @DisplayName("A simple alert can be read and accepted")
    void alert() {
        driver.get(BASE + "/javascript_alerts");
        driver.findElement(By.cssSelector("button[onclick='jsAlert()']")).click();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        assertTrue(alert.getText().contains("I am a JS Alert"));
        alert.accept();
    }

    @Test
    @DisplayName("Dismissing a confirm is reflected on the page")
    void confirmDismiss() {
        driver.get(BASE + "/javascript_alerts");
        driver.findElement(By.cssSelector("button[onclick='jsConfirm()']")).click();
        // dismiss() presses Cancel; accept() would press OK.
        wait.until(ExpectedConditions.alertIsPresent()).dismiss();
        assertTrue(driver.findElement(By.id("result")).getText().contains("You clicked: Cancel"));
    }

    @Test
    @DisplayName("Entering prompt text is reflected on the page")
    void promptAccept() {
        driver.get(BASE + "/javascript_alerts");
        driver.findElement(By.cssSelector("button[onclick='jsPrompt()']")).click();
        // Only prompt() dialogs accept text.
        Alert prompt = wait.until(ExpectedConditions.alertIsPresent());
        prompt.sendKeys("Selenium");
        prompt.accept();
        assertTrue(driver.findElement(By.id("result")).getText().contains("Selenium"));
    }
}
