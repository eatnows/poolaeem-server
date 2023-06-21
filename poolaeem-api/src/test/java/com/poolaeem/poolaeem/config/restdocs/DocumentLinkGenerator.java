package com.poolaeem.poolaeem.config.restdocs;

public interface DocumentLinkGenerator {
    static String generateLinkCode(DocUrl docUrl) {
        return String.format("link:../common/$s.html[%s %s, role=\"pop\"]", docUrl.pageId, docUrl.text);
    }

    enum DocUrl {
        ;

        private final String pageId;
        private final String text;

        DocUrl(String pageId, String text) {
            this.pageId = pageId;
            this.text = text;
        }
    }
}
