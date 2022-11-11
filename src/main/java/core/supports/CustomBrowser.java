package core.supports;

import core.utils.web.WebEntity;
import core.wrappers.WebDriver;

public final class CustomBrowser {
    private final WebDriver webDriver;

    public CustomBrowser(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebEntity openPage(String url) {
        webDriver.openUrl(url);

        return new WebEntity(webDriver.getRootElement());
    }

    public void close() {
        webDriver.quit();
    }

    public WebEntity refresh() {
        webDriver.refresh();

        return new WebEntity(webDriver.getRootElement());
    }
}
