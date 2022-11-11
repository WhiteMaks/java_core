package core;

import core.enums.BrowserType;
import core.supports.CustomBrowser;
import core.supports.CustomJsonParser;
import core.supports.CustomLogger;
import core.utils.WebDriverManager;
import core.utils.OperationSystem;
import core.utils.WebDriverManager;
import core.utils.cmd.kubectl.Kubectl;
import core.wrappers.DatabaseDriver;
import core.wrappers.RestApi;
import core.wrappers.WebDriver;
import core.wrappers.impl.ExtGson;
import core.wrappers.impl.ExtJDBCDriver;
import core.wrappers.impl.ExtLogback;
import core.wrappers.impl.ExtRestAssured;
import core.wrappers.impl.ExtSeleniumDriver;

import java.util.HashMap;
import java.util.Map;

public final class CoreFactory {
    private static CoreFactory coreFactory;

    private final Map<Class<?>, CustomLogger> customLoggers;
    private final Map<String, CustomBrowser> customBrowsers;

    private OperationSystem operationSystem;

    private CoreFactory() {
        customLoggers = new HashMap<>();
        customBrowsers = new HashMap<>();
    }

    public static CoreFactory getInstance() {
        if (coreFactory != null) {
            return coreFactory;
        }
        synchronized (CoreFactory.class) {
            if (coreFactory == null) {
                coreFactory = new CoreFactory();
            }
            return coreFactory;
        }
    }

    public CustomLogger createLogger(Class<?> clazz) {
        if (customLoggers.containsKey(clazz)) {
            return customLoggers.get(clazz);
        }

        var logger = new CustomLogger(new ExtLogback(clazz));
        customLoggers.put(clazz, logger);
        return logger;
    }

    public CustomJsonParser createJsonParser() {
        return new CustomJsonParser(new ExtGson());
    }

    public DatabaseDriver createDatabaseDriver() {
        return new ExtJDBCDriver();
    }

    public CustomBrowser createBrowser(BrowserType browserType) {
        var threadName = Thread.currentThread()
                .getName();

        var browserInMap = customBrowsers.get(threadName);
        if (browserInMap != null) {
            return browserInMap;
        }

        var pathToDriverFolder = "./.drivers";
        var webDriverManager = new WebDriverManager(
                getOperationSystem(),
                browserType
        );

        webDriverManager.setPathToDriverFolder(pathToDriverFolder);
        webDriverManager.deleteDriverIfVersionNotCorrect();
        if (!webDriverManager.driverIsFoundInto()) {
            webDriverManager.downloadDriverInto();
        }

        customBrowsers.put(
                threadName,
                new CustomBrowser(
                        new ExtSeleniumDriver(webDriverManager)
                )
        );

        return customBrowsers.get(threadName);
    }

    public Kubectl createKubectl() {
        return new Kubectl(getOperationSystem().getCmd());
    }

    public RestApi createRestApi() {
        return new ExtRestAssured();
    }

    public OperationSystem getOperationSystem() {
        if (operationSystem == null) {
            operationSystem = new OperationSystem();
        }
        return operationSystem;
    }

}
