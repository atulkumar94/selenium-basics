package com.selenium.basics;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/** S3. WebElement state and form interaction. */
class S03FormsTest extends BaseTest {

    @Test
    @DisplayName("Number input accepts a value readable via the DOM property")
    void numberInput() {
        driver.get(BASE + "/inputs");
        WebElement number = driver.findElement(By.cssSelector("input[type='number']"));
        number.clear();
        number.sendKeys("42");
        // getDomProperty("value") is the live value; getDomAttribute("value") is the
        // value written in the HTML and would not change after typing.
        assertEquals("42", number.getDomProperty("value"));
    }

    @Test
    @DisplayName("Every checkbox can be driven to the selected state")
    void checkboxesAllSelected() {
        driver.get(BASE + "/checkboxes");
        List<WebElement> boxes = driver.findElements(By.cssSelector("#checkboxes input[type='checkbox']"));
        // click() toggles, so only click the boxes that are not already checked.
        for (WebElement box : boxes) {
            if (!box.isSelected()) {
                box.click();
            }
        }
        assertTrue(boxes.stream().allMatch(WebElement::isSelected));
    }

    @Test
    @DisplayName("A form field accepts input and its submit button is actionable")
    void formFieldAndSubmit() {
        driver.get(BASE + "/forgot_password");
        WebElement email = driver.findElement(By.id("email"));
        email.sendKeys("tester@example.com");
        assertEquals("tester@example.com", email.getDomProperty("value"));

        // isDisplayed + isEnabled is the usual "can a user click this?" check.
        WebElement submit = driver.findElement(By.id("form_submit"));
        assertTrue(submit.isDisplayed() && submit.isEnabled());
        submit.click();
    }
}
