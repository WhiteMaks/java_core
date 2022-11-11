package core.wrappers;

public interface Json {

    <T> T getObjectFromJson(String json, Class<T> clazz);

    String getJsonFromObject(Object object);

}
