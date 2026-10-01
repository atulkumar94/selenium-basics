package com.selenium.basics;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

/**
 * S13-S16. Browser-state capability as a stand-in for the architecture topics.
 *
 * Test-runner integration (this JUnit project is itself the example), parallel
 * execution on a Selenium Grid via RemoteWebDriver, Selenium 4 BiDi/CDP network
 * events and CI artifact publishing all require a separate runner or environment
 * rather than a page on this fixture site.
 */
class S13ArchitectureNotesTest extends BaseTest {

    @Test
    @DisplayName("The window can be positioned and reports a sane size")
    void windowState() {
        driver.get(SITE_URL);
        // manage().window() controls the OS window: position, size, maximize, fullscreen.
        driver.manage().window().setPosition(new Point(0, 0));
        Dimension size = driver.manage().window().getSize();
        assertTrue(size.getWidth() > 0 && size.getHeight() > 0);
    }
}
