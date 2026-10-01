# Selenium WebDriver Learning Plan

A hands-on curriculum for Selenium WebDriver, using [The Internet](https://the-internet.hackerearth.com/) as the main browser-practice site.

Mark a module complete after implementing its exercise and being able to explain the key tradeoffs. The site is a practice fixture, not a complete Selenium feature catalog: use a small local HTML fixture or a test environment for features it does not expose (such as a true native multi-select, Selenium Grid, or BiDi).

## Practice Site Map

| Skill | Site page |
|---|---|
| Dynamic/stable locators, buttons and tables | [Challenging DOM](https://the-internet.hackerearth.com/challenging_dom), [Sortable Data Tables](https://the-internet.hackerearth.com/tables) |
| Checkbox state | [Checkboxes](https://the-internet.hackerearth.com/checkboxes) |
| Native select | [Dropdown](https://the-internet.hackerearth.com/dropdown) |
| Mouse, keyboard, scrolling and input | [Hovers](https://the-internet.hackerearth.com/hovers), [Context Menu](https://the-internet.hackerearth.com/context_menu), [Drag and Drop](https://the-internet.hackerearth.com/drag_and_drop), [Key Presses](https://the-internet.hackerearth.com/key_presses), [Inputs](https://the-internet.hackerearth.com/inputs), [Horizontal Slider](https://the-internet.hackerearth.com/horizontal_slider), [Infinite Scroll](https://the-internet.hackerearth.com/infinite_scroll), [Floating Menu](https://the-internet.hackerearth.com/floating_menu) |
| Waits and changing DOM | [Dynamic Controls](https://the-internet.hackerearth.com/dynamic_controls), [Dynamic Loading](https://the-internet.hackerearth.com/dynamic_loading), [Dynamic Content](https://the-internet.hackerearth.com/dynamic_content), [Disappearing Elements](https://the-internet.hackerearth.com/disappearing_elements), [Slow Resources](https://the-internet.hackerearth.com/slow), [Add/Remove Elements](https://the-internet.hackerearth.com/add_remove_elements/) |
| Windows, frames, alerts and Shadow DOM | [Multiple Windows](https://the-internet.hackerearth.com/windows), [Frames](https://the-internet.hackerearth.com/frames), [Nested Frames](https://the-internet.hackerearth.com/nested_frames), [JavaScript Alerts](https://the-internet.hackerearth.com/javascript_alerts), [Shadow DOM](https://the-internet.hackerearth.com/shadowdom) |
| Authentication and files | [Form Authentication](https://the-internet.hackerearth.com/login), [Basic Auth](https://the-internet.hackerearth.com/basic_auth), [Digest Authentication](https://the-internet.hackerearth.com/digest_auth), [File Upload](https://the-internet.hackerearth.com/upload), [File Download](https://the-internet.hackerearth.com/download), [Secure File Download](https://the-internet.hackerearth.com/download_secure) |
| Browser/DOM edge cases | [Broken Images](https://the-internet.hackerearth.com/broken_images), [Shifting Content](https://the-internet.hackerearth.com/shifting_content), [Large & Deep DOM](https://the-internet.hackerearth.com/large), [WYSIWYG Editor](https://the-internet.hackerearth.com/tinymce), [Geolocation](https://the-internet.hackerearth.com/geolocation), [Notification Messages](https://the-internet.hackerearth.com/notification_message) |

## Curriculum

### S1. WebDriver architecture and session lifecycle
- [ ] `WebDriver`, browser-specific drivers, `RemoteWebDriver`, browser session, W3C WebDriver protocol, Selenium Manager, and driver/browser compatibility.
- [ ] Start a browser, navigate, inspect the current URL/title, and always quit in teardown. Distinguish `close()` from `quit()`.
- [ ] Configure browser options: headless mode, arguments, preferences, downloads, certificates, proxy, page-load strategy, and capabilities.
- [ ] Exercise: create a browser factory for Chrome/Firefox and run a smoke test against the site. Explain why application code should usually depend on `WebDriver`, not a concrete driver class.

### S2. Locators and element lookup
- [ ] All standard strategies: `id`, `name`, `className`, `tagName`, `linkText`, `partialLinkText`, `cssSelector`, and `xpath`.
- [ ] `findElement` versus `findElements`; uniqueness, zero matches, stale matches, scoped search, relative locators, and XPath axes/functions.
- [ ] Prefer accessible, stable attributes and semantic locators; handle dynamic IDs, changing text, whitespace, localization, and ambiguous matches.
- [ ] Exercise: locate a specific row/action on Challenging DOM and a table cell on Sortable Data Tables; assert locator cardinality and avoid positional selectors where possible.

### S3. WebElement state and forms
- [ ] `click`, `sendKeys`, `clear`, `getText`, `getAttribute`, `getDomAttribute`, `getDomProperty`, `isDisplayed`, `isEnabled`, and `isSelected`.
- [ ] Text inputs, number/date controls, links, buttons, labels, checkboxes, radio buttons, and form submission.
- [ ] Exercise: update text/number inputs, toggle checkboxes only when necessary, verify final state, and submit Form Authentication.

### S4. Synchronization, waits, and timeouts
- [ ] Implicit, explicit (`WebDriverWait`/`ExpectedConditions`), and fluent waits; polling, ignored exceptions, timeout design, and why fixed sleeps are brittle.
- [ ] Presence versus visibility versus clickability; text/value changes, invisibility, alert/frame/window conditions, and stale-element synchronization.
- [ ] Avoid mixing implicit and explicit waits without understanding compounded timing. Re-find elements after re-render instead of retrying a stale reference.
- [ ] Exercise: wait for add/remove on Dynamic Controls and completion on Dynamic Loading; compare DOM presence with visibility.

### S5. Mouse, keyboard, and composite actions
- [ ] `Actions`: hover, move, click, double-click, context-click, click-and-hold, drag-and-drop, key down/up, chorded keys, offsets, and `perform()`.
- [ ] Wheel/scroll actions where supported; browser/page scrolling alternatives; native drag-and-drop limitations and diagnosis.
- [ ] Exercise: hover profile controls, use the context menu, drag items, change the slider, and send keyboard sequences on the corresponding site pages.

### S6. Dropdowns and selection
- [ ] Native `<select>` with Selenium `Select`: `selectByVisibleText`, `selectByValue`, `selectByIndex`, `getOptions`, selected options, `isMultiple`, and deselection APIs.
- [ ] Custom dropdowns are ordinary DOM controls: open, wait for the intended option, click, then verify its selected/value state.
- [ ] Multi-select works only when the HTML `<select>` has the `multiple` attribute. The site's Dropdown page is not a true multi-select exercise; build a local fixture for multiple selection and `deselectAll()`.
- [ ] Exercise: select and verify each native dropdown option, then implement both native multi-select and custom multi-select examples using separate fixtures.

### S7. Navigation and browsing contexts
- [ ] `get`, back, forward, refresh, URL/title checks, and navigation/page-load timeouts.
- [ ] Windows/tabs: handles are unordered; capture the original handle, detect the new one by set difference, switch, close, and return.
- [ ] Frames/iframes: switch by locator/name/index, parent frame versus default content, nested traversal, and context-related locator failures.
- [ ] Exercise: complete the Multiple Windows, Frames, and Nested Frames examples, returning cleanly to the original context.

### S8. JavaScript dialogs and browser prompts
- [ ] `Alert`: accept, dismiss, read text, and enter prompt text. Distinguish JavaScript alerts from HTML modals and browser-native permission prompts.
- [ ] Exercise: cover alert, confirm, and prompt flows on JavaScript Alerts; verify page state after handling each.

### S9. Dynamic DOM, tables, and robust state checks
- [ ] Re-rendering, stale references, asynchronous loading, disappearing/reappearing elements, overlays, shifting layout, infinite scroll, and changing content.
- [ ] Dynamic tables: row scoping, headers/cells, sorting, duplicate text, nested tables, and data extraction.
- [ ] Exercise: compare Dynamic Content across refreshes; locate row actions in Sortable Data Tables; load more Infinite Scroll content without fixed sleeps.

### S10. Shadow DOM, JavaScript, screenshots, and browser state
- [ ] Selenium 4 `getShadowRoot()` and scoped `SearchContext` traversal; reacquire host/root/leaf after component replacement.
- [ ] `JavascriptExecutor` for async scripts, scrolling, and diagnosis; use native WebDriver interactions first and avoid JS clicks that mask actionability bugs.
- [ ] Screenshots at driver/element level, page source, cookies, local/session storage access, and browser logs where supported.
- [ ] Exercise: inspect Shadow DOM, capture evidence on failure, and use JS only for a justified operation.

### S11. Files, authentication, and browser capabilities
- [ ] Upload with `sendKeys()` to a file input; handle absolute paths, remote/Grid file detectors, and upload verification.
- [ ] Download configuration and verification (download directory, completion, filename/content); secure downloads may require authentication setup.
- [ ] Authentication: form-based, HTTP Basic/Digest, credentials handling, and avoiding secrets in source/logs.
- [ ] Exercise: upload a fixture file, download and verify an artifact, and test the site's authentication examples. Do not commit credentials or generated files.

### S12. Exceptions and failure diagnosis
- [ ] `NoSuchElementException`, `StaleElementReferenceException`, `ElementClickInterceptedException`, `ElementNotInteractableException`, `TimeoutException`, `NoSuchFrameException`, and alert/window errors.
- [ ] Diagnose locator, synchronization, DOM state, overlay, browser context, and test-data causes separately; preserve screenshot, URL, page source, and logs.
- [ ] Exercise: deliberately create one failure per category on a relevant page and write the root cause plus durable fix.

### S13. Test design and automation architecture
- [ ] Selenium is browser automation, not a test runner. Integrate with JUnit/TestNG; assertions, setup/teardown, parameterization, tags, and reusable fixtures.
- [ ] Page Object Model, page components, service/helper boundaries, meaningful intent-level APIs, and avoiding giant base classes or trivial wrappers.
- [ ] Deterministic test data, independent tests, cleanup, secrets/configuration, and avoiding assertions hidden inside page objects.
- [ ] Exercise: implement a small page object for login and a reusable component for a repeated navigation area; keep test intent readable.

### S14. Parallel execution, Selenium Grid, and remote runs
- [ ] Thread safety: one WebDriver session per test/thread; lifecycle cleanup; shared test data and download/artifact collision risks.
- [ ] Grid architecture (router, distributor, nodes, sessions), `RemoteWebDriver`, capabilities, browser matrix, Docker, and diagnosing remote-only failures.
- [ ] Exercise: run the same smoke test across browser configurations. Grid itself needs a local/containerized Grid or hosted environment, not a site page.

### S15. Selenium 4 BiDi/CDP and network-adjacent capabilities
- [ ] Know the boundary between classic WebDriver and browser-specific CDP; understand Selenium BiDi's event-driven direction and support differences.
- [ ] Browser console/network events, downloads, permissions, and interception only where supported by the selected browser/Selenium version.
- [ ] Exercise: create a separate capability spike against a controlled local test server; do not assume every command works cross-browser.

### S16. CI, reliability, accessibility, and interview scenarios
- [ ] Headless execution, CI artifacts, browser version pinning, retries as diagnostics rather than a flake fix, test reports, and reproducible environments.
- [ ] Cross-browser coverage and responsive checks; Selenium can drive accessibility checks but is not itself a complete accessibility audit tool.
- [ ] Explain flakiness triage, suite runtime, risk-based test selection, and when API/component tests are a better fit than browser tests.
- [ ] Exercise: run a short, independent smoke suite and publish screenshots/logs on failure. CI/Grid requires a separate runner environment.

## Completion Checklist

- [ ] Implement one useful example for each site-supported interaction.
- [ ] Add local fixtures for missing controls such as a genuine `<select multiple>`.
- [ ] Explain locator choice, synchronization, state verification, and cleanup for each scenario.
- [ ] Demonstrate a cross-browser run and describe the separate setup needed for Grid/BiDi/CI.