package core.supports;

import core.CoreFactory;
import core.utils.RestResponse;
import core.wrappers.RestApi;

import java.util.Map;

public abstract class CustomRestApi {
    private final static CustomLogger logger = CoreFactory.getInstance().createLogger(CustomRestApi.class);

    private final RestApi restApi;
    private final CustomJsonParser jsonParser;

    public CustomRestApi(RestApi restApi) {
        this.restApi = restApi;

        jsonParser = CoreFactory.getInstance().createJsonParser();
    }

    protected RestResponse sendGet(String url) {
        logger.trace("<< Отправка GET запроса по url [ " + url + " ]");
        return restApi.sendGet(url);
    }

    protected RestResponse sendGet(String url, Map<String, String> headers) {
        logger.trace("<< Отправка GET запроса по url [ " + url + " ]");
        return restApi.sendGet(url, headers);
    }

    protected RestResponse sendJsonPost(String url, Object body) {
        logger.trace("<< Отправка POST запроса по url [ " + url + " ]");
        logger.trace("<< Тело запроса [ " + jsonParser.parseFromObject(body) + " ]");
        return restApi.sendPostJson(url, body);
    }

    protected RestResponse sendJsonPost(String url, Map<String, String> headers, Object body) {
        logger.trace("<< Отправка POST запроса по url [ " + url + " ]");
        logger.trace("<< Тело запроса [ " + jsonParser.parseFromObject(body) + " ]");
        return restApi.sendPostJson(url, headers, body);
    }
}
