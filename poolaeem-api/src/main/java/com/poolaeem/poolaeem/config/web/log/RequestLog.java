package com.poolaeem.poolaeem.config.web.log;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.Part;
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class RequestLog {
    private final String method;
    private final String url;
    private final Map<String, Object> headers;
    private final Map<String, Object> cookies;
    private final Map<String, Object> parameters;
    private final String requestBody;
    private final MultiValueMap<String, String> files = new LinkedMultiValueMap<>();

    public RequestLog(CustomCachingRequestWrapper requestWrapper) {
        this.method = requestWrapper.getMethod();
        this.url = requestWrapper.getRequestURI();

        this.headers = Collections.list(requestWrapper.getHeaderNames())
                .stream()
                .filter(name -> !StringUtils.equals(name, "cookie"))
                .collect(Collectors.toMap(
                        name -> name,
                        name -> getValue(Collections.list(requestWrapper.getHeaders(name)))
                ));

        this.cookies = Arrays.stream(requestWrapper.getCookies() == null ? new Cookie[0]: requestWrapper.getCookies())
                .collect(Collectors.groupingBy(Cookie::getName))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> getValue(entry.getValue().stream().map(Cookie::getValue).collect(Collectors.toList()))
                ));

        this.parameters = requestWrapper.getParameterMap().entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> getValue(Arrays.asList(entry.getValue()))
                ));

        this.requestBody = toString(requestWrapper.getInputStream());

        for (Part part : requestWrapper.getParts()) {
            if (part.getSubmittedFileName() == null) continue;
            files.add(part.getName(), part.getSubmittedFileName());
        }
    }

    private String toString(ServletInputStream in) {
        try {
            return IOUtils.toString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Object getValue(List<String> values) {
        return values.size() == 1 ? values.get(0) : values;
    }
}
