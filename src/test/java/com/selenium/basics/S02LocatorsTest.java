package com.selenium.basics;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.locators.RelativeLocator;

/** S2. Locators and element lookup across every strategy. */
class S02LocatorsTest extends BaseTest {

    @Test
    @DisplayName("All locator strategies resolve the same heading node")
    void allStrategiesResolveSameNode() {
        driver.get(SITE_URL);
        WebElement byClass = driver.findElement(By.className("heading"));
        WebElement byCss = driver.findElement(By.cssSelector("h1.heading"));
        WebElement byXpath = driver.findElement(By.xpath("//h1[contains(@class,'heading')]"));
        // WebElement.equals() compares the underlying DOM node, not the locator used.
        assertEquals(byClass, byCss);
        assertEquals(byCss, byXpath);

        // tagName returns the first matching tag on the page.
        WebElement byTag = driver.findElement(By.tagName("h1"));
        assertFalse(byTag.getText().isBlank());
    }

    @Test
    @DisplayName("linkText and partialLinkText and findElements all work")
    void linkAndCollectionLocators() {
        driver.get(SITE_URL);
        // linkText needs the exact visible text; partialLinkText matches a substring.
        assertEquals("Checkboxes", driver.findElement(By.linkText("Checkboxes")).getText());
        assertTrue(driver.findElement(By.partialLinkText("Drag and")).getText().contains("Drag"));

        // findElements returns every match (an empty list, not an exception, when none match).
        List<WebElement> links = driver.findElements(By.cssSelector("#content ul li a"));
        assertTrue(links.size() > 20, "expected many example links");
    }

    @Test
    @DisplayName("byId equals byName, and a relative locator finds the submit button")
    void idNameAndRelativeLocators() {
        driver.get(BASE + "/forgot_password");
        WebElement byId = driver.findElement(By.id("email"));
        WebElement byName = driver.findElement(By.name("email"));
        assertEquals(byId, byName);

        // Selenium 4 relative locator: "the button that is below the email field".
        WebElement submit = driver.findElement(By.id("form_submit"));
        WebElement below = driver.findElement(RelativeLocator.with(By.tagName("button")).below(byId));
        assertEquals(submit, below);
    }
}
