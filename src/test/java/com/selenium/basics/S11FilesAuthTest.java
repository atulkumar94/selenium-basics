package com.selenium.basics;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/** S11. File upload, download links and HTTP Basic authentication. */
class S11FilesAuthTest extends BaseTest {

    @Test
    @DisplayName("A file can be uploaded and its name confirmed")
    void fileUpload() throws Exception {
        File fixture = File.createTempFile("selenium-upload", ".txt");
        fixture.deleteOnExit();
        Files.writeString(fixture.toPath(), "uploaded by selenium tour");

        driver.get(BASE + "/upload");
        // No OS file dialog: send the absolute path straight to the <input type="file">.
        driver.findElement(By.id("file-upload")).sendKeys(fixture.getAbsolutePath());
        driver.findElement(By.id("file-submit")).click();
        assertTrue(driver.findElement(By.id("uploaded-files")).getText().contains(fixture.getName()));
    }

    @Test
    @DisplayName("The download page exposes downloadable links")
    void downloadLinksPresent() {
        driver.get(BASE + "/download");
        // Clicking one of these would save into target/downloads (see BaseTest prefs).
        List<WebElement> downloads = driver.findElements(By.cssSelector("#content a"));
        assertFalse(downloads.isEmpty());
    }

    @Test
    @DisplayName("HTTP Basic Auth succeeds with credentials in the URL")
    void basicAuth() {
        // The browser's auth popup is not part of the page, so Selenium can't type
        // into it. Putting user:password@ in the URL skips the popup.
        driver.get("https://admin:admin@the-internet.hackerearth.com/basic_auth");
        assertTrue(driver.findElement(By.cssSelector("#content p")).getText().contains("Congratulations"));
    }
}
