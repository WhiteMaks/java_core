package core.wrappers;

import java.util.Set;

public interface WebDriver {

    void quit();

    void refresh();

    void openUrl(String url);

    void switchToWindow(String window);

    void executeJsScript(String script);

    String getCurrentUrl();

    String getCurrentWindowHandle();

    WebElement getRootElement();

    Set<String> getAllWindowHandles();

}
