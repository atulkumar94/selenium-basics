package com.learning.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** S1. WebDriver architecture, options and session lifecycle. */
class S01SessionLifecycleTest extends BaseTest {

    @Test
    @DisplayName("Opens the home page and reports title and URL")
    void opensHomePage() {
        // get() blocks until the page has loaded (page load strategy NORMAL).
        driver.get(SITE_URL);
        assertFalse(driver.getTitle().isBlank(), "title should not be blank");
        assertTrue(driver.getCurrentUrl().contains("the-internet.hackerearth.com"));
    }

    @Test
    @DisplayName("Exercises back, forward and refresh navigation history")
    void navigatesHistory() {
        String abTest = BASE + "/abtest";
        driver.get(SITE_URL);

        // navigate().to() does the same as get() but reads better next to back/forward.
        driver.navigate().to(abTest);
        assertEquals(abTest, driver.getCurrentUrl());

        driver.navigate().back();
        assertEquals(SITE_URL, driver.getCurrentUrl());

        driver.navigate().forward();
        assertEquals(abTest, driver.getCurrentUrl());

        // refresh() reloads the page but keeps the URL.
        driver.navigate().refresh();
        assertEquals(abTest, driver.getCurrentUrl());
    }
}
