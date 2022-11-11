package core.wrappers.impl;

import com.google.gson.Gson;
import core.wrappers.Json;

public class ExtGson implements Json {
    private final Gson gson;

    public ExtGson() {
        gson = new Gson();
    }

    @Override
    public <T> T getObjectFromJson(String json, Class<T> clazz) {
        return gson.fromJson(
                json,
                clazz
        );
    }

    @Override
    public String getJsonFromObject(Object object) {
        return gson.toJson(object);
    }
}
