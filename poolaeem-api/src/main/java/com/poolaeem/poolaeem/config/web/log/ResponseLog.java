package com.poolaeem.poolaeem.config.web.log;

import lombok.Getter;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class ResponseLog {
    private int status;
    private Map<String, String[]> headers;
    private String responseBody;

    public ResponseLog(CustomCachingResponseWrapper responseWrapper) throws IOException {
        this.status = responseWrapper.getStatus();

        this.headers = new HashMap<>();
        for (String headerName : responseWrapper.getHeaderNames()) {
                headers.put(headerName, responseWrapper.getHeaders(headerName).toArray(String[]::new));
        }
//        this.headers = responseWrapper.getHeaderNames()
//                .stream()
//                .collect(Collectors.toMap(
//                        name -> name,
//                        name -> responseWrapper.getHeaders(name).toArray(String[]::new)
//                ));

        this.responseBody = toString(responseWrapper.getContentInputStream());
    }

    private String toString(InputStream in) {
        try {
            String responseBody = IOUtils.toString(in, StandardCharsets.UTF_8);
            if (responseBody.startsWith("<!DOCTYPE html>")) {
                return "<!DOCTYPE html>";
            }
            return responseBody;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
