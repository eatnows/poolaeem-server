package com.poolaeem.poolaeem.config.web.log;

import lombok.Getter;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class ResponseLog {
    private int status;
    private Map<String, String> headers;
    private String responseBody;

    public ResponseLog(CustomCachingResponseWrapper responseWrapper) throws IOException {
        this.status = responseWrapper.getStatus();

        this.headers = responseWrapper.getHeaderNames()
                .stream()
                .collect(Collectors.toMap(
                        name -> name,
                        responseWrapper::getHeader,
                        (s1, s2) -> s1
                ));

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
