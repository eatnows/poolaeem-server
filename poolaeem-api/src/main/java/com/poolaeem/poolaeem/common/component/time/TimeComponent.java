package com.poolaeem.poolaeem.common.component.time;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeComponent {
    public static final String MILLISECOND_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mmXXX";
    public static final String DEFAULT_TIMEZONE = "Asia/Seoul";

    public static ZonedDateTime nowUtc() {
        return ZonedDateTime.now(ZoneId.of(ZoneOffset.UTC.getId()));
    }

    public static ZonedDateTime convertUtcToKst(ZonedDateTime time) {
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        return ZonedDateTime.of(time.plus(TimeZone.getTimeZone(zoneId).getRawOffset(), ChronoUnit.MILLIS).toLocalDateTime(), zoneId);
    }
}
