package org.example.context;

import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

import static java.lang.ThreadLocal.withInitial;


public enum TestContext {

    CONTEXT;

    private static final String RESPONSE = "RESPONSE";
    private final ThreadLocal<Map<String, Object>> testContexts = withInitial(HashMap::new);

    public <T> T get(String name) {
        return (T) testContexts.get()
                .get(name);
    }

    public <T> T set(String name, T object) {
        testContexts.get()
                .put(name, object);
        return object;
    }


    public Response getResponse() {
        return get(RESPONSE);
    }

    public Response setResponse(Response response) {
        return set(RESPONSE, response);
    }

    public void reset() {
        testContexts.get()
                .clear();
    }
}

