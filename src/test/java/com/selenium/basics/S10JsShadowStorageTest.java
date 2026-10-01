package com.selenium.basics;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

/** S10. Shadow DOM, JavascriptExecutor, cookies, storage and screenshots. */
class S10JsShadowStorageTest extends BaseTest {

    @Test
    @DisplayName("A shadow root can be traversed to reach a slotted node")
    void shadowDomTraversal() {
        driver.get(BASE + "/shadowdom");
        // Normal locators can't see inside a shadow root. Find the host element, open
        // its shadow root, then search inside it (CSS selectors only, not XPath).
        WebElement host = driver.findElement(By.tagName("my-paragraph"));
        SearchContext shadow = host.getShadowRoot();
        WebElement slot = shadow.findElement(By.cssSelector("slot"));
        assertEquals("slot", slot.getTagName());
    }

    @Test
    @DisplayName("JavascriptExecutor returns a value from the page")
    void javascriptExecutor() {
        driver.get(SITE_URL);
        // Without "return" the script runs but gives back null.
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        // With "return", JS numbers come back as Long or Double, so read them as Number.
        Number height = (Number) js.executeScript("return document.body.scrollHeight");
        assertTrue(height.intValue() > 0);
    }

    @Test
    @DisplayName("Cookies and web storage can be written and read back")
    void cookiesAndStorage() {
        // Cookies and storage belong to a domain, so open a page on it first.
        driver.get(SITE_URL);
        driver.manage().addCookie(new Cookie("tour", "selenium"));
        assertEquals("selenium", driver.manage().getCookieNamed("tour").getValue());

        // WebDriver has no storage API of its own, so go through JavaScript.
        js.executeScript("window.localStorage.setItem('k','v'); window.sessionStorage.setItem('s','1');");
        assertEquals("v", js.executeScript("return window.localStorage.getItem('k')"));
        assertEquals("1", js.executeScript("return window.sessionStorage.getItem('s')"));
    }

    @Test
    @DisplayName("Driver and element screenshots are captured to disk")
    void screenshots() throws Exception {
        driver.get(SITE_URL);
        Path dir = Files.createDirectories(Path.of("target/screenshots"));

        // getScreenshotAs(FILE) writes to a temp file, so copy it somewhere permanent.
        // The page shot shows the visible part of the page; an element shot shows only that element.
        Path page = Files.copy(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE).toPath(),
                dir.resolve("page.png"), StandardCopyOption.REPLACE_EXISTING);
        Path heading = Files.copy(driver.findElement(By.tagName("h1")).getScreenshotAs(OutputType.FILE).toPath(),
                dir.resolve("h1.png"), StandardCopyOption.REPLACE_EXISTING);

        assertTrue(Files.size(page) > 0);
        assertTrue(Files.size(heading) > 0);
    }
}
