package org.example.apicalls.client;


import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.core.Cookie;

import java.util.ArrayList;
import java.util.List;

public class AddCookieClientFilter implements ClientRequestFilter {

    private Cookie cookie;

    public AddCookieClientFilter(Cookie cookie) {
        super();
        this.cookie = cookie;
    }

    @Override
    public void filter(ClientRequestContext clientRequestContext) {
        List<Object> cookies = new ArrayList<>();
        cookies.add(this.cookie);
        clientRequestContext.getCookies().entrySet().stream().forEach(item -> cookies.add(item.getValue()));
        clientRequestContext.getHeaders().put("Cookie", cookies);
    }
}

