package com.connect4;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class RestActions {
    private final MockMvc mvc;
    private final ObjectMapper objectMapper;

    public RestActions(MockMvc mvc, ObjectMapper objectMapper) {
        this.mvc = mvc;
        this.objectMapper = objectMapper;
    }

    public ResultActions getAs(String urlTemplate, int playerId) {
        return translateException(() -> mvc.perform(MockMvcRequestBuilders.get(urlTemplate).accept(APPLICATION_JSON).headers(ssoHeader(playerId))));
    }

    public ResultActions get(String urlTemplate) {
        return translateException(() -> mvc.perform(MockMvcRequestBuilders.get(urlTemplate).accept(APPLICATION_JSON)));
    }

    public ResultActions post(String urlTemplate) {
        return post(urlTemplate, null, new HttpHeaders());
    }

    public ResultActions post(String urlTemplate, Object requestBody) {
        return post(urlTemplate, requestBody, new HttpHeaders());
    }

    public ResultActions postAs(String urlTemplate, int playerId) {
        return post(urlTemplate, null, ssoHeader(playerId));
    }

    private HttpHeaders ssoHeader(int playerId) {
        return httpHeader("sso", playerId);
    }

    public ResultActions post(String urlTemplate, Object requestBody, HttpHeaders httpHeaders) {
        return translateException(() -> mvc.perform(MockMvcRequestBuilders.post(urlTemplate)
                .headers(httpHeaders)
                .content(toJson(requestBody))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)));
    }

    protected HttpHeaders httpHeader(String name, Object value) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(name, value.toString());
        return httpHeaders;
    }

    protected String toJson(Object o) {
        return translateException(() -> objectMapper.writeValueAsString(o));
    }

    protected <T> T responseAs(ResultActions result, Class<T> clazz) {
        return translateException(() -> objectMapper.readValue(result.andReturn().getResponse().getContentAsByteArray(), clazz));
    }

    protected <T> T translateException(CallableWithException<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected interface CallableWithException<T> {
        T call() throws Exception;
    }
}
