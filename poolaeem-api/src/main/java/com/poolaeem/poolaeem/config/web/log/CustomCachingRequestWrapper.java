package com.poolaeem.poolaeem.config.web.log;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.Part;
import lombok.Getter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
public class CustomCachingRequestWrapper extends HttpServletRequestWrapper {

    private byte[] body;
    private Map<String, String[]> parameterMap;
    private Collection<Part> parts;

    public CustomCachingRequestWrapper(HttpServletRequest request) throws UnsupportedEncodingException {
        super(request);
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try {
            if (request.getContentType() == null) {
                this.parts = new ArrayList<>();
            } else {
                try {
                    this.parts = request.getParts();
                } catch (ServletException e) {
                    this.parts = new ArrayList<>();
                }
            }
            this.parameterMap = request.getParameterMap();
            this.body = request.getInputStream().readAllBytes();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public ServletInputStream getInputStream() {
        return new ByteArrayServletInputStream(this.body);
    }

    private static class ByteArrayServletInputStream extends ServletInputStream {
        private ByteArrayInputStream in;

        public ByteArrayServletInputStream(byte[] body) {
            this.in = new ByteArrayInputStream(body);
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }

        @Override
        public int read() {
            return in.read();
        }
    }
}
