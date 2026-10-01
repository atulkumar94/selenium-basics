package com.selenium.basics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

/** S6. Native dropdown selection via the Select helper. */
class S06DropdownTest extends BaseTest {

    @Test
    @DisplayName("Select reports a single-selection dropdown with options")
    void dropdownMetadata() {
        driver.get(BASE + "/dropdown");
        // Select only works on a native <select> element, not on custom div dropdowns.
        Select select = new Select(driver.findElement(By.id("dropdown")));
        assertFalse(select.isMultiple());
        // "Please select an option", "Option 1", "Option 2"
        assertEquals(3, select.getOptions().size());
    }

    @Test
    @DisplayName("Options can be chosen by visible text, value and index")
    void selectByTextValueIndex() {
        driver.get(BASE + "/dropdown");
        Select select = new Select(driver.findElement(By.id("dropdown")));

        select.selectByVisibleText("Option 1");
        assertEquals("Option 1", select.getFirstSelectedOption().getText());

        // Matches the option's value="2" attribute, not its text.
        select.selectByValue("2");
        assertEquals("Option 2", select.getFirstSelectedOption().getText());

        // Index is 0-based and counts the placeholder at index 0.
        select.selectByIndex(1);
        assertEquals("Option 1", select.getFirstSelectedOption().getText());
    }
}
