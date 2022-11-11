package core.wrappers;

import core.utils.RestResponse;

import java.util.Map;

public interface RestApi {

    RestResponse sendGet(String url);

    RestResponse sendGet(String url, Map<String, String> headers);

    RestResponse sendPostJson(String url, Object body);

    RestResponse sendPostJson(String url, Map<String, String> headers, Object body);

}
