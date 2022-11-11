package core.utils.web.elements;

import core.wrappers.WebElement;

public class ButtonWebElement {
    private final WebElement element;

    public ButtonWebElement(WebElement element) {
        this.element = element;
    }

    public boolean isFound() {
        return element.isFound();
    }

    public boolean click() {
        return element.click();
    }

    public boolean isEnabled() {
        return element.isEnabled();
    }

    public boolean isVisible() {
        return element.isDisplayed();
    }

    public String getName() {
        return element.getText();
    }
}
