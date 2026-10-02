package com.learning.selenium;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/** S9. Dynamic DOM, tables and robust state checks. */
class S09DynamicDomTest extends BaseTest {

    @Test
    @DisplayName("Elements can be added and removed, with counts verified")
    void addAndRemoveElements() {
        driver.get(BASE + "/add_remove_elements/");
        By added = By.cssSelector(".added-manually");

        WebElement addBtn = driver.findElement(By.cssSelector("button[onclick='addElement()']"));
        for (int i = 0; i < 3; i++) {
            addBtn.click();
        }
        // Re-query with findElements each time: a list fetched earlier would not
        // reflect elements added or removed since.
        assertEquals(3, driver.findElements(added).size());

        // Each added "Delete" button removes itself when clicked.
        driver.findElement(added).click();
        assertEquals(2, driver.findElements(added).size());
    }

    @Test
    @DisplayName("A column of table data can be extracted")
    void extractTableColumn() {
        driver.get(BASE + "/tables");
        // td:first-child picks the first cell of every row, which is the "Last Name" column.
        List<WebElement> lastNames = driver.findElements(By.cssSelector("#table1 tbody tr td:first-child"));
        assertEquals(4, lastNames.size());
        assertTrue(lastNames.stream().anyMatch(c -> c.getText().equals("Smith")));
    }

    @Test
    @DisplayName("Dynamic content renders non-empty text")
    void dynamicContentRenders() {
        driver.get(BASE + "/dynamic_content");
        // The text changes on every load, so assert on shape (not blank), not exact value.
        String content = driver.findElement(By.cssSelector("#content .row .large-10")).getText();
        assertFalse(content.isBlank());
    }
}
