package com.learning.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.remote.Browser;

/**
 * S14. Options and capabilities. BaseTest builds the browser-specific options;
 * these tests read back what the session actually negotiated.
 */
class S14OptionsCapabilitiesTest extends BaseTest {

    /** Options are what we asked for; these capabilities are what the browser agreed to. */
    private Capabilities capabilities() {
        return ((HasCapabilities) driver).getCapabilities();
    }

    @Test
    @DisplayName("Session runs the browser chosen with -Dbrowser")
    void sessionMatchesRequestedBrowser() {
        Capabilities caps = capabilities();
        Browser expected = switch (BROWSER) {
            case "firefox" -> Browser.FIREFOX;
            case "edge" -> Browser.EDGE;
            case "safari" -> Browser.SAFARI;
            default -> Browser.CHROME;
        };
        // Browser.is() handles naming differences, e.g. Edge reports "MicrosoftEdge".
        assertTrue(expected.is(caps), "expected " + expected.browserName() + " but was " + caps.getBrowserName());
        assertFalse(caps.getBrowserVersion().isBlank(), "browser version should be reported");
    }

    @Test
    @DisplayName("Page load strategy set in the options is honoured by the session")
    void pageLoadStrategyIsApplied() {
        assertEquals("normal", String.valueOf(capabilities().getCapability("pageLoadStrategy")));
    }
}
