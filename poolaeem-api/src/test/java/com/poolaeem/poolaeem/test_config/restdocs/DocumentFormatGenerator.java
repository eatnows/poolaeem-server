package com.poolaeem.poolaeem.test_config.restdocs;

import org.springframework.restdocs.snippet.Attributes;

import static org.springframework.restdocs.snippet.Attributes.key;

public interface DocumentFormatGenerator {
    static Attributes.Attribute getDateTimeFormat() {
        return key("format").value("yyyy-MM-ddTHH:mmXXX");
    }
    static Attributes.Attribute getMilliSecondFormat() {
        return key("format").value("yyyy-MM-ddTHH:mm:ss.SSSXXX");
    }
}
