package com.selenium.basics;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** S7. Switching browsing contexts: windows and frames. */
class S07NavigationContextTest extends BaseTest {

    @Test
    @DisplayName("Opens a second window, reads it, and returns to the original")
    void multipleWindows() {
        driver.get(BASE + "/windows");
        String original = driver.getWindowHandle();
        driver.findElement(By.linkText("Click Here")).click();
        // The new window opens asynchronously, so wait for it before switching.
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        // Selenium does not switch automatically: the new handle is the one that
        // isn't the original.
        Set<String> handles = driver.getWindowHandles();
        handles.remove(original);
        driver.switchTo().window(handles.iterator().next());
        assertEquals("New Window", driver.findElement(By.tagName("h3")).getText());

        // close() closes only the current window. The driver must then be
        // switched back explicitly, or later calls fail with NoSuchWindowException.
        driver.close();
        driver.switchTo().window(original);
        assertTrue(driver.getCurrentUrl().endsWith("/windows"));
    }

    @Test
    @DisplayName("Traverses nested frames to read the middle frame")
    void nestedFrames() {
        driver.get(BASE + "/nested_frames");
        // Frames are entered one level at a time, here by name.
        driver.switchTo().frame("frame-top");
        driver.switchTo().frame("frame-middle");
        assertEquals("MIDDLE", driver.findElement(By.id("content")).getText());
        // defaultContent() jumps back to the top document; parentFrame() goes up one level.
        driver.switchTo().defaultContent();
    }

    @Test
    @DisplayName("Edits text inside the WYSIWYG iframe editor")
    void iframeEditor() {
        driver.get(BASE + "/tinymce");
        // Waits for the iframe to load and switches into it in one step.
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("mce_0_ifr")));
        WebElement body = driver.findElement(By.id("tinymce"));
        assertTrue(body.isDisplayed(), "editor body is reachable inside the iframe");

        // Back to the top document, then drive the editor through its own API,
        // which is more robust than typing past the page's overlay elements.
        driver.switchTo().defaultContent();
        js.executeScript("tinymce.activeEditor.setContent('Edited inside an iframe by Selenium');");
        String content = (String) js.executeScript("return tinymce.activeEditor.getContent();");
        assertTrue(content.contains("Edited inside an iframe"));
    }
}
