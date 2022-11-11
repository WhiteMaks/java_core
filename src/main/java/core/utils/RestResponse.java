package core.utils;

import core.CoreFactory;
import core.supports.CustomLogger;

public class RestResponse {
    private final static CustomLogger logger = CoreFactory.getInstance().createLogger(RestResponse.class);

    private String contentType;
    private String body;

    private Integer statusCode;
    private Long time;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        logger.trace(">> Код ответа [ " + statusCode + " ]");
        this.statusCode = statusCode;
    }

    public String getBodyString() {
        return body;
    }

    public <T> T getBodyByClass(Class<T> responseClass) {
        T t = null;
        try {
            t = CoreFactory.getInstance()
                    .createJsonParser()
                    .parseFromJson(
                            body,
                            responseClass
                    );
        } catch (Exception ex) {
            logger.warn(
                    "Ошибка при попытке сохранить тело ответа в виде объекта класса [ " + responseClass.getName() + " ]. Метод возвращает null!",
                    ex
            );
        }
        return t;
    }

    public void setBody(String body) {
        logger.trace(">> Тело ответа [ " + body + " ]");
        this.body = body;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        logger.trace(">> Время ответа [ " + time + " ] ms");
        this.time = time;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        logger.trace(">> Формат ответа [ " + contentType + " ]");
        this.contentType = contentType;
    }
}
