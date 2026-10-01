package com.learning.selenium;

import java.io.File;
import java.time.Duration;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Shared Selenium lifecycle for every topic test. A fresh browser session is
 * created before each test and quit afterwards so tests stay independent.
 *
 * -Dbrowser=chrome|firefox|edge|safari (default chrome), -Dheadless=true for CI.
 */
public abstract class BaseTest {

    protected static final String BASE = "https://the-internet.hackerearth.com";
    protected static final String SITE_URL = BASE + "/";

    // Read once from -D system properties; static is safe because they never change.
    protected static final String BROWSER = System.getProperty("browser", "chrome").toLowerCase();
    protected static final boolean HEADLESS = Boolean.getBoolean("headless");

    // Instance fields, not static: WebDriver is not thread-safe, and JUnit creates a
    // new test instance per method, so each parallel test gets its own browser.
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    protected JavascriptExecutor js;
    protected File downloadDir;

    @BeforeEach
    void setUp() {
        downloadDir = new File("target/downloads");
        downloadDir.mkdirs();

        driver = createDriver();
        if (!HEADLESS) {
            driver.manage().window().maximize();
        }
        // Implicit wait applies to every findElement call; explicit waits (below) are
        // for specific conditions. Mixing large values of both can stretch timeouts.
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        actions = new Actions(driver);
        js = (JavascriptExecutor) driver;
    }

    @AfterEach
    void tearDown() {
        // quit() ends the whole session and closes every window; close() would only
        // close the current window and leave the driver process running.
        if (driver != null) {
            driver.quit();
        }
    }

    private WebDriver createDriver() {
        // No driver binaries are configured: Selenium Manager downloads a matching one.
        return switch (BROWSER) {
            case "chrome" -> new ChromeDriver(chromiumOptions(new ChromeOptions()));
            case "edge" -> new EdgeDriver(chromiumOptions(new EdgeOptions()));
            case "firefox" -> new FirefoxDriver(firefoxOptions());
            case "safari" -> new SafariDriver(safariOptions());
            default -> throw new IllegalArgumentException(
                    "Unknown -Dbrowser=" + BROWSER + " (use chrome, firefox, edge or safari)");
        };
    }

    /** Chrome and Edge are both Chromium, so they share arguments and prefs. */
    private <T extends ChromiumOptions<T>> T chromiumOptions(T options) {
        // NORMAL waits for the load event before get() returns (S14 checks this).
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        if (HEADLESS) {
            // Headless has no screen to maximize into, so give it an explicit size.
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        // Save downloads straight into target/downloads without a "Save as" dialog.
        options.setExperimentalOption("prefs", Map.of(
                "download.default_directory", downloadDir.getAbsolutePath(),
                "download.prompt_for_download", false,
                "safebrowsing.enabled", true));
        return options;
    }

    private FirefoxOptions firefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        if (HEADLESS) {
            options.addArguments("-headless", "--width=1920", "--height=1080");
        }
        // folderList=2 means "use browser.download.dir" instead of the default Downloads folder
        options.addPreference("browser.download.folderList", 2);
        options.addPreference("browser.download.dir", downloadDir.getAbsolutePath());
        // Save these MIME types without asking.
        options.addPreference("browser.helperApps.neverAsk.saveToDisk",
                "application/octet-stream,application/pdf,text/plain,image/jpeg,image/png");
        return options;
    }

    /** Safari has no headless mode and no download prefs; run `safaridriver --enable` once first. */
    private SafariOptions safariOptions() {
        SafariOptions options = new SafariOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        return options;
    }
}
