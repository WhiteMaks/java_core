package core.wrappers.impl;

import core.utils.WebDriverManager;
import core.wrappers.WebDriver;
import core.wrappers.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Collections;
import java.util.Set;

public class ExtSeleniumDriver implements WebDriver {
    private final org.openqa.selenium.WebDriver driver;

    public ExtSeleniumDriver(WebDriverManager driverManager) {

        switch (driverManager.getBrowserType()) {
            default -> driver = initChromeDriver(driverManager.getFullPathToDriver());
        }
    }

    @Override
    public void quit() {
        driver.quit();
    }

    @Override
    public void refresh() {
        driver.navigate()
                .refresh();
    }

    @Override
    public void openUrl(String url) {
        driver.get(url);
    }

    @Override
    public void switchToWindow(String window) {
        driver.switchTo()
                .window(window);
    }

    @Override
    public void executeJsScript(String script) {
        var javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript(script);
    }

    @Override
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Override
    public String getCurrentWindowHandle() {
        return driver.getWindowHandle();
    }

    @Override
    public WebElement getRootElement() {
        return new ExtSeleniumElement(
                driver.findElement(
                        By.xpath("//*")
                )
        );
    }

    @Override
    public Set<String> getAllWindowHandles() {
        return driver.getWindowHandles();
    }

    private org.openqa.selenium.WebDriver initChromeDriver(String pathToDriver) {
        System.setProperty("webdriver.chrome.driver", pathToDriver);

        var options = new ChromeOptions();
        options.addArguments("--dns-prefetch-disable");
        options.addArguments("--start-maximized");
        options.addArguments("--ignore-certificate-errors");
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));

        return new ChromeDriver(options);
    }
}
