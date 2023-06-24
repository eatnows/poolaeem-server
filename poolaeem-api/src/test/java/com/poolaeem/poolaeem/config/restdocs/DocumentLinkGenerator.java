package com.poolaeem.poolaeem.config.restdocs;

public interface DocumentLinkGenerator {
    static String generateLinkCode(DocUrl docUrl) {
        return String.format("link:../common/$s.html[%s %s, role=\"pop\"]", docUrl.pageId, docUrl.text);
    }

    enum DocUrl {
        OAUTH_PROVIDER("oauth_provider", "OAuth2 Provider")
        ;

        private final String pageId;
        private final String text;

        DocUrl(String pageId, String text) {
            this.pageId = pageId;
            this.text = text;
        }
    }
}
