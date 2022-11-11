package core.utils.api;

import core.CoreFactory;
import core.supports.CustomLogger;
import core.supports.CustomRestApi;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ChromedriverStorageApi extends CustomRestApi {
    private final static CustomLogger logger = CoreFactory.getInstance().createLogger(ChromedriverStorageApi.class);

    private final String url;

    public ChromedriverStorageApi() {
        super(CoreFactory.getInstance().createRestApi());

        url = "https://chromedriver.storage.googleapis.com/";
    }

    public String getLatestDriverRelease(String chromeVersion) {
        logger.debug("Начат процесс определения последней стабильной версии драйвера для Google Chrome " + chromeVersion);

        var result = sendGet(url.concat("LATEST_RELEASE_").concat(chromeVersion)).getBodyString();

        logger.debug("Определена последняя стабильная версия драйвера [ " + result + " ] для Google Chrome " + chromeVersion);

        return result;
    }

    public InputStream getDriverFile(String version, String file) {
        try {
            logger.debug("Открытие потока для получения файла драйвера [ " + file + " ] версии [ " + version + " ]");

            return new URL(url.concat(version).concat("/").concat(file)).openStream();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
