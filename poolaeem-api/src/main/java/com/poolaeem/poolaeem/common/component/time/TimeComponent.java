package com.poolaeem.poolaeem.common.component.time;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeComponent {
    public static ZonedDateTime nowUtc() {
        return ZonedDateTime.now(ZoneId.of(ZoneOffset.UTC.getId()));
    }
}
