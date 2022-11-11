package core.utils;

import core.CoreFactory;
import core.enums.BrowserType;
import core.enums.OSType;
import core.supports.CustomLogger;
import core.utils.api.ChromedriverStorageApi;

import java.io.File;

public class WebDriverManager {
    private final static CustomLogger logger = CoreFactory.getInstance().createLogger(WebDriverManager.class);

    private final OperationSystem operationSystem;
    private final BrowserType browserType;
    private final OSType osType;

    private final ChromedriverStorageApi chromedriverStorageApi;

    private String pathToDriverFolder;

    public WebDriverManager(OperationSystem operationSystem, BrowserType browserType) {
        this.browserType = browserType;
        this.operationSystem = operationSystem;

        osType = operationSystem.getType();

        chromedriverStorageApi = new ChromedriverStorageApi();
    }

    public void deleteDriverIfVersionNotCorrect() {
        if (!driverIsFoundInto()) {
            return;
        }

        getBrowserVersion();
    }

    public boolean driverIsFoundInto() {
        return new File(getFullPathToDriver())
                .isFile();
    }

    public void downloadDriverInto() {
        switch (browserType) {
            case CHROME -> {
                var latestDriverRelease = chromedriverStorageApi.getLatestDriverRelease(getBrowserVersion());

                var driveFileName = browserType.getDriverName()
                        .concat("_")
                        .concat(getOsNameForChromedriver())
                        .concat(".zip");

                var driverFile = chromedriverStorageApi.getDriverFile(
                        latestDriverRelease,
                        driveFileName
                );

                new File(getFullPathWithoutDriver()).mkdirs();

                new ZIPFile(driverFile)
                        .unpackFile(getFullPathToDriver());

            }
        }
    }

    public String getFullPathWithoutDriver() {
        return pathToDriverFolder + "/"
                + browserType.getDriverName() + "/"
                + osType.toString().toLowerCase();
    }

    public String getFullPathToDriver() {
        return getFullPathWithoutDriver() + "/"
                + getFullDriverName();
    }

    public String getFullDriverName() {
        if (osType == OSType.WINDOWS) {
            return browserType.getDriverName() + ".exe";
        }

        return browserType.getDriverName();
    }

    public String getBrowserVersion() {
        String result = null;

        switch (browserType) {
            case CHROME -> result = getChromeVersion();
        }

        return result;
    }

    private String getChromeVersion() {
        logger.debug("Начат процесс определения версии для установленного Google Chrome");

        var fullVersion = executeCheckVersionChrome();

        var correctVersion = fullVersion.substring(
                0,
                fullVersion.length() - 3
        );

        logger.debug("Определена версия [ " + correctVersion + " ] для установленного Google Chrome");

        return correctVersion;
    }

    public BrowserType getBrowserType() {
        return browserType;
    }

    private String executeCheckVersionChrome() {
        String result;

        var cmd = operationSystem.getCmd();

        switch (osType) {
            case WINDOWS -> {
                var output = cmd.execute("reg query \"HKEY_CURRENT_USER\\Software\\Google\\Chrome\\BLBeacon\" /v version")
                        .getSuccess();

                result = output.replaceAll(
                        "\\s+",
                        " "
                ).split(" ")[4];
            }
            default -> throw new RuntimeException("Не реализована возможность получения версии для [ Google Chrome ] на операционной системе [ " + osType + " ]");
        }

        return result;
    }

    private String getOsNameForChromedriver() {
        String result = null;

        switch (osType) {
            case WINDOWS -> result = "win32";
            case LINUX -> result = "linux64";
            case MAC_OS -> result = "mac64";
        }

        return result;
    }

    public void setPathToDriverFolder(String pathToDriverFolder) {
        this.pathToDriverFolder = pathToDriverFolder;
    }
}
