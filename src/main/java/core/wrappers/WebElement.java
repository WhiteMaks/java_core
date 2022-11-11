package core.wrappers;

import java.util.List;

public interface WebElement {

    boolean click();

    boolean clear();

    boolean isEnabled();

    boolean isSelected();

    boolean isFound();

    boolean isDisplayed();

    boolean sendKeys(CharSequence... var1);

    String getText();

    String getValue();

    String visualHTMLElement();

    String getAttribute(String attribute);

    WebElement findParentElement();

    WebElement findElementById(String id);

    WebElement findElementByType(String type);

    WebElement findElementByXpath(String xpath);

    WebElement findElementByTagName(String tagName);

    WebElement findElementByClassName(String className);

    WebElement findElementByCssSelector(String cssSelector);

    List<WebElement> getAllChildElements();

    List<WebElement> findElementsByXpath(String xpath);

    List<WebElement> findElementsByTagName(String tagName);

    List<WebElement> findElementsByClassName(String className);

}
