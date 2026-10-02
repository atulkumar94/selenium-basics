# Selenium Basics

Runnable Selenium 4 + JUnit 5 examples, one test class per topic. Every test runs against the public fixture site [the-internet.hackerearth.com](https://the-internet.hackerearth.com).

## Tech stack

| Tool | Version |
|------|---------|
| Java | 17 |
| Selenium Java | 4.50.0 |
| JUnit Jupiter | 6.1.3 |
| Maven Surefire | 3.6.0 |
| Browser | Chrome by default; Firefox, Edge or Safari with `-Dbrowser` (drivers are resolved automatically by Selenium Manager) |

## Project layout

```
.
├── pom.xml
└── src/test
    ├── java/com/selenium/basics
    │   ├── BaseTest.java            # browser choice, options, setup and teardown
    │   └── S01…S14*Test.java        # one class per topic
    └── resources
        └── junit-platform.properties  # parallel execution settings
```

## Running the tests

```bash
mvn clean test                          # run everything (in parallel, 4 at a time)
mvn test -Dheadless=true                # run without opening browser windows
mvn test -Dbrowser=firefox              # run on another browser: chrome (default), firefox, edge, safari
mvn test -Dtest=S04WaitsTest            # run one class
mvn test -Dtest=S04WaitsTest#explicitWaitEnablesInput   # run one test method
```

## BaseTest

[BaseTest.java](src/test/java/com/selenium/basics/BaseTest.java) is the parent of every topic class. Before each test it:

- starts a fresh driver for the browser chosen with `-Dbrowser` (maximized, or headless with `-Dheadless=true`)
- sets the download folder to `target/downloads` so downloads don't prompt

### Browser options

Each browser gets its own options object, built in `BaseTest`:

| Browser | Options class | What is configured |
|---------|---------------|--------------------|
| Chrome, Edge | `ChromeOptions`, `EdgeOptions` (both `ChromiumOptions`) | `--headless=new` and window size, download prefs via `setExperimentalOption("prefs", ...)` |
| Firefox | `FirefoxOptions` | `-headless` with `--width`/`--height`, download prefs via `addPreference(...)` |
| Safari | `SafariOptions` | Page load strategy only. Safari has no headless mode or download prefs. Run `safaridriver --enable` once before using it. Safari allows only one session at a time, so add `-Djunit.jupiter.execution.parallel.enabled=false` |

All of them set `PageLoadStrategy.NORMAL`, which S14 checks.
- sets a 5s implicit wait and a 30s page-load timeout
- creates the shared helpers `wait` (10s `WebDriverWait`), `actions` (`Actions`) and `js` (`JavascriptExecutor`)

After each test it calls `driver.quit()`. Because each test gets its own browser, tests don't depend on each other, which is also what makes parallel execution safe.

## Topics

| # | Class | Topic | What the tests show |
|---|-------|-------|---------------------|
<<<<<<< HEAD
| S1 | [S01SessionLifecycleTest](src/test/java/com/selenium/basics/S01SessionLifecycleTest.java) | WebDriver architecture and session lifecycle | Opening a page, reading the title and URL, using back, forward and refresh |
| S2 | [S02LocatorsTest](src/test/java/com/selenium/basics/S02LocatorsTest.java) | Locators | `className`, `cssSelector`, `xpath`, `tagName`, `linkText`, `partialLinkText`, `id`, `name`, `findElements`, Selenium 4 relative locators |
| S3 | [S03FormsTest](src/test/java/com/selenium/basics/S03FormsTest.java) | WebElement state and forms | `clear` and `sendKeys`, `getDomProperty`, checkbox `isSelected`, submit buttons |
| S4 | [S04WaitsTest](src/test/java/com/selenium/basics/S04WaitsTest.java) | Synchronization | `WebDriverWait` with `ExpectedConditions` (invisibility, clickable) and `FluentWait` polling |
| S5 | [S05ActionsTest](src/test/java/com/selenium/basics/S05ActionsTest.java) | Actions API | Hover, context click, key presses, moving a slider, the limits of drag-and-drop with HTML5 |
| S6 | [S06DropdownTest](src/test/java/com/selenium/basics/S06DropdownTest.java) | Dropdowns | The `Select` helper: `isMultiple`, `getOptions`, selecting by visible text, value and index |
| S7 | [S07NavigationContextTest](src/test/java/com/selenium/basics/S07NavigationContextTest.java) | Windows and frames | Window handles, `switchTo().window`, nested frames, editing inside an iframe |
| S8 | [S08AlertsTest](src/test/java/com/selenium/basics/S08AlertsTest.java) | JavaScript dialogs | `alert`, `confirm` and `prompt` with `accept`, `dismiss` and `sendKeys` |
| S9 | [S09DynamicDomTest](src/test/java/com/selenium/basics/S09DynamicDomTest.java) | Dynamic DOM and tables | Adding and removing elements, reading a table column, dynamic content |
| S10 | [S10JsShadowStorageTest](src/test/java/com/selenium/basics/S10JsShadowStorageTest.java) | JS, Shadow DOM, storage and screenshots | `getShadowRoot`, `executeScript`, cookies, local and session storage, page and element screenshots |
| S11 | [S11FilesAuthTest](src/test/java/com/selenium/basics/S11FilesAuthTest.java) | Files and authentication | Uploading with `sendKeys`, download links, HTTP Basic Auth with credentials in the URL |
| S12 | [S12ExceptionsTest](src/test/java/com/selenium/basics/S12ExceptionsTest.java) | Common exceptions | `NoSuchElementException`, `TimeoutException`, `StaleElementReferenceException` |
| S13 | [S13ArchitectureNotesTest](src/test/java/com/selenium/basics/S13ArchitectureNotesTest.java) | Framework architecture | Window position and size. Its notes cover test-runner integration, Grid and `RemoteWebDriver`, BiDi/CDP and CI artifacts, which need their own environment |
| S14 | [S14OptionsCapabilitiesTest](src/test/java/com/selenium/basics/S14OptionsCapabilitiesTest.java) | Options and capabilities | Reads back the negotiated capabilities (browser name, version, page load strategy) to confirm the options from `BaseTest` were applied |

## Parallel execution

Selenium has no built-in parallel runner. Parallel runs come from the test framework, and here JUnit 5 handles them. Settings live in [junit-platform.properties](src/test/resources/junit-platform.properties):

```properties
junit.jupiter.execution.parallel.enabled=true
junit.jupiter.execution.parallel.mode.default=same_thread
junit.jupiter.execution.parallel.mode.classes.default=concurrent
junit.jupiter.execution.parallel.config.strategy=fixed
junit.jupiter.execution.parallel.config.fixed.parallelism=4
junit.jupiter.execution.parallel.config.fixed.max-pool-size=4
```

What each setting does:

| Property | Effect |
|----------|--------|
| `parallel.enabled=true` | Turns on parallel execution |
| `mode.classes.default=concurrent` | Different test classes run at the same time |
| `mode.default=same_thread` | Methods inside one class run one after another |
| `config.strategy=fixed` + `fixed.parallelism=4` | Uses a fixed pool of 4 threads, so at most 4 browsers are open |
| `fixed.max-pool-size=4` | Hard cap. It stops JUnit from adding threads while tests are blocked waiting on the browser |

### Changing it

- **Different thread count:** change both `fixed.parallelism` and `fixed.max-pool-size`, or override them for one run:
  ```bash
  mvn test -Djunit.jupiter.execution.parallel.config.fixed.parallelism=2 \
           -Djunit.jupiter.execution.parallel.config.fixed.max-pool-size=2
  ```
- **Run in sequence** (useful when debugging):
  ```bash
  mvn test -Djunit.jupiter.execution.parallel.enabled=false
  ```
- **Run methods in parallel too:** set `mode.default=concurrent`. Each method already gets its own browser, so this is safe, but it opens more browsers at once.

### Common mistakes

- **Leaving out `.config.` in property names.** JUnit silently ignores unknown keys. For example, `junit.jupiter.execution.parallel.strategy` does nothing, and JUnit falls back to one thread per CPU core, so you get far more browsers than you expected.
- **Setting Surefire's `<parallel>` / `<threadCount>`.** Those options only apply to JUnit 4 and TestNG. With JUnit 5, configure parallelism in `junit-platform.properties`.
- **Making `driver` static or sharing it.** `WebDriver` is not thread-safe. Keep it as an instance field, as `BaseTest` does. JUnit creates a new test instance for each method, so every thread gets its own browser.
- **Writing to shared files.** All tests share `target/downloads`. If two parallel tests download a file with the same name, one can overwrite the other.

### Going further: Selenium Grid

To run on several machines or browsers, start a [Selenium Grid](https://www.selenium.dev/documentation/grid/) and replace `new ChromeDriver(options)` in `BaseTest` with:

```java
driver = new RemoteWebDriver(new URL("http://localhost:4444"), options);
```

JUnit still decides how many tests run at once. Grid decides where each browser runs.
