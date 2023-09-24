package com.poolaeem.poolaeem.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
public class OAuth2AuthorizedClientConfig {

    private final JdbcTemplate jdbcTemplate;
    private final ClientRegistrationRepository clientRegistrationRepository;

    public OAuth2AuthorizedClientConfig(JdbcTemplate jdbcTemplate, ClientRegistrationRepository clientRegistrationRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    protected OAuth2AuthorizedClientService oauth2AuthorizedClientService() {
        return new JdbcOAuth2AuthorizedClientService(jdbcTemplate, clientRegistrationRepository);
    }
}
