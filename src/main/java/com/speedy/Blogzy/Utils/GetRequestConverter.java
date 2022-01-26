package com.speedy.Blogzy.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class GetRequestConverter extends HttpServletRequestWrapper {
    public GetRequestConverter(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getMethod() {
        return "GET";
    }
}
