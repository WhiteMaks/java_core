package core.supports;

import core.wrappers.Json;

public final class CustomJsonParser {
    private final Json json;

    public CustomJsonParser(Json json) {
        this.json = json;
    }

    public <T> T parseFromJson(String jsonBody, Class<T> clazz) {
        return json.getObjectFromJson(jsonBody, clazz);
    }

    public String parseFromObject(Object object) {
        return json.getJsonFromObject(object);
    }
}
