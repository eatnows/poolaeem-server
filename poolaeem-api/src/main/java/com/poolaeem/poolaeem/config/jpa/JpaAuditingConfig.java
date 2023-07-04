package com.poolaeem.poolaeem.config.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "userIdAuditorAware",
        dateTimeProviderRef = "dateTimeProvider")
@Configuration
public class JpaAuditingConfig {
    @Bean
    public AuditorAware<String> userIdAuditorAware() {
        return new UserIdAuditorAware();
    }

    @Bean
    public DateTimeProvider dateTimeProvider() {
        return new DateTimeAuditingProvider();
    }
}
