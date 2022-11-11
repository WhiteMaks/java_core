package core.utils.web.elements;

import core.wrappers.WebElement;

public class InputWebElement {
    private final WebElement element;

    public InputWebElement(WebElement element) {
        this.element = element;
    }

    public boolean setValue(String value) {
        return element.sendKeys(value);
    }

    public boolean isFound() {
        return element.isFound();
    }

    public boolean clear() {
        return element.clear();
    }

    public String getValue() {
        return element.getValue();
    }
}
