package com.poolaeem.poolaeem.config.web.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class RequestResponseLogFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            CustomCachingRequestWrapper requestWrapper = new CustomCachingRequestWrapper(request);
            CustomCachingResponseWrapper responseWrapper = new CustomCachingResponseWrapper(response);

            log.info("[Request] {}", objectMapper.writeValueAsString(new RequestLog(requestWrapper)));

            filterChain.doFilter(requestWrapper, responseWrapper);

            log.info("[Response] {}", objectMapper.writeValueAsString(new ResponseLog(responseWrapper)));

            responseWrapper.copyBodyToResponse();
        }
    }
}
