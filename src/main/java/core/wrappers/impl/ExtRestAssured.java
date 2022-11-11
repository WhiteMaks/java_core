package core.wrappers.impl;

import core.utils.RestResponse;
import core.wrappers.RestApi;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

public class ExtRestAssured implements RestApi {

    public ExtRestAssured() {
        RestAssured.config = RestAssuredConfig.config();
    }

    @Override
    public RestResponse sendGet(String url) {
        return sendGet(url, null);
    }

    @Override
    public RestResponse sendGet(String url, Map<String, String> headers) {
        return getRestResponse(
                getRequestSpecification()
                        .headers(headers == null ? new HashMap<String, String>() : headers)
                        .relaxedHTTPSValidation()
                        .get(url)
        );
    }

    @Override
    public RestResponse sendPostJson(String url, Object body) {
        return sendPostJson(
                url,
                null,
                body
        );
    }

    @Override
    public RestResponse sendPostJson(String url, Map<String, String> headers, Object body) {
        return getRestResponse(
                getRequestSpecification()
                        .contentType(ContentType.JSON)
                        .headers(headers == null ? new HashMap<String, String>() : headers)
                        .body(
                                body,
                                ObjectMapperType.GSON
                        )
                        .relaxedHTTPSValidation()
                        .post(url)
        );
    }

    private RequestSpecification getRequestSpecification() {
        return RestAssured.given();
    }

    private RestResponse getRestResponse(Response response) {
        var restResponse = new RestResponse();
        restResponse.setTime(response.getTime());
        restResponse.setStatusCode(response.getStatusCode());
        restResponse.setContentType(response.getContentType().toLowerCase());
        restResponse.setBody(response.getBody().asString());
        return restResponse;
    }
}
