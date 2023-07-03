package com.poolaeem.poolaeem.config.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poolaeem.poolaeem.config.web.log.RequestResponseLogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;

    public WebMvcConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public FilterRegistrationBean requestLogFilterBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new RequestResponseLogFilter(objectMapper));
        registrationBean.setOrder(Integer.MIN_VALUE);

        registrationBean.setUrlPatterns(List.of("/*"));
        return registrationBean;
    }

}
