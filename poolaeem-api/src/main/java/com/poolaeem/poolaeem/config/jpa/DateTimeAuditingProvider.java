package com.poolaeem.poolaeem.config.jpa;

import org.springframework.data.auditing.DateTimeProvider;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

public class DateTimeAuditingProvider implements DateTimeProvider {
    @Override
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(ZonedDateTime.now(ZoneId.of(ZoneOffset.UTC.getId())));
    }
}
