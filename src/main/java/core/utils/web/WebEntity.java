package core.utils.web;

import core.supports.Common;
import core.utils.web.elements.ButtonWebElement;
import core.utils.web.elements.InputWebElement;
import core.wrappers.WebElement;

public class WebEntity {
    private final WebElement rootElement;

    public WebEntity(WebElement rootElement) {
        this.rootElement = rootElement;
    }

    public InputWebElement findInputByName(String name) {
        return new InputWebElement(
                rootElement.findElementByXpath(".//input[contains(@name, '" + name + "')]")
        );
    }

    public boolean setInputValue(InputWebElement input, String value) {
        if (!input.isFound()) {
            return false;
        }

        return input.setValue(value);
    }

    public String getInputValue(InputWebElement input) {
        if (!input.isFound()) {
            return null;
        }

        return input.getValue();
    }

    public boolean waitButtonInvisible(ButtonWebElement button, int waitSeconds) {
        long waitMillis = waitSeconds * 1000L;

        while (button.isVisible() && waitMillis != 0) {
            Common.sleep(100);
            waitMillis -= 100;
        }

        return waitMillis != 0 && !button.isVisible();
    }

    public boolean clickButton(ButtonWebElement button) {
        if (!button.isFound()) {
            return false;
        }

        return button.click();
    }

    public ButtonWebElement findButtonByName(String name) {
        var tagButton = findButtonByName(
                "button",
                name
        );

        if (tagButton.isFound()) {
            return tagButton;
        }

        var tagInput = findButtonByName(
                "input",
                name
        );

        if (tagInput.isFound()) {
            return tagInput;
        }

        return findButtonByName(
                "a",
                name
        );
    }

    private ButtonWebElement findButtonByName(String tagName, String name) {
        var buttonElement = rootElement.findElementByXpath(".//" + tagName + "[contains(text(), '" + name + "')]");

        if (buttonElement.isFound()) {
            return new ButtonWebElement(buttonElement);
        }

        buttonElement = rootElement.findElementByXpath(".//" + tagName + "[contains(., '" + name + "')]");

        if (buttonElement.isFound()) {
            return new ButtonWebElement(buttonElement);
        }

        return new ButtonWebElement(
                rootElement.findElementByXpath(".//" + tagName + "[contains(@value, '" + name + "')]")
        );
    }

    public WebElement getRootElement() {
        return rootElement;
    }
}
