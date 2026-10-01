package com.learning.selenium;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** S5. Mouse, keyboard and composite Actions. */
class S05ActionsTest extends BaseTest {

    @Test
    @DisplayName("Hovering a figure reveals its caption")
    void hoverRevealsCaption() {
        driver.get(BASE + "/hovers");
        WebElement figure = driver.findElement(By.cssSelector(".figure"));
        // Actions only build a sequence; nothing happens until perform().
        actions.moveToElement(figure).perform();
        WebElement caption = figure.findElement(By.cssSelector(".figcaption"));
        assertTrue(caption.isDisplayed());
    }

    @Test
    @DisplayName("Context-click opens a JavaScript alert")
    void contextClickOpensAlert() {
        driver.get(BASE + "/context_menu");
        // contextClick = right click. This page answers it with a JS alert.
        actions.contextClick(driver.findElement(By.id("hot-spot"))).perform();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        assertTrue(alert.getText().contains("context menu"));
        alert.accept();
    }

    @Test
    @DisplayName("Key press is reflected on the page")
    void keyPressIsReported() {
        driver.get(BASE + "/key_presses");
        // The input sits in a <form>, so ENTER would submit; use a non-submitting key.
        driver.findElement(By.id("target")).sendKeys(Keys.SPACE);
        assertTrue(wait.until(
                ExpectedConditions.textToBePresentInElementLocated(By.id("result"), "SPACE")));
    }

    @Test
    @DisplayName("Horizontal slider moves off zero with arrow keys")
    void sliderMoves() {
        driver.get(BASE + "/horizontal_slider");
        WebElement slider = driver.findElement(By.cssSelector("input[type='range']"));
        // Click to focus the slider, then each ARROW_RIGHT moves it one step.
        slider.click();
        for (int i = 0; i < 5; i++) {
            slider.sendKeys(Keys.ARROW_RIGHT);
        }
        // The page shows the current value in #range.
        String value = driver.findElement(By.id("range")).getText();
        assertTrue(Double.parseDouble(value) > 0.0, "slider should have advanced");
    }

    @Test
    @DisplayName("Drag-and-drop columns are present (native HTML5 DnD may not move via Actions)")
    void dragAndDropColumnsPresent() {
        driver.get(BASE + "/drag_and_drop");
        WebElement columnA = driver.findElement(By.id("column-a"));
        WebElement columnB = driver.findElement(By.id("column-b"));
        // Actions simulate mouse events, but HTML5 drag-and-drop listens for
        // dragstart/drop events, so the columns often don't actually swap.
        // That's why this test only checks that the action runs without error.
        actions.dragAndDrop(columnA, columnB).perform();
        assertTrue(columnA.isDisplayed() && columnB.isDisplayed());
    }
}
