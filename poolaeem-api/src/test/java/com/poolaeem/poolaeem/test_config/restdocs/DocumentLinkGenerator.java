package com.poolaeem.poolaeem.test_config.restdocs;

public interface DocumentLinkGenerator {
    static String generateLinkCode(DocUrl docUrl) {
        return String.format("link:../common/%s.html[%s %s,role=\"popup\"]", docUrl.pageId, docUrl.text, "목록");
    }

    enum DocUrl {
        OAUTH_PROVIDER("oauth_provider", "OAuth2 Provider"),
        WORKBOOK_THEME("workbook_theme", "문제집 테마"),
        PROBLEM_TYPE("problem_type", "문항 종류"),
        WORD_LANGUAGE("word_language", "단어 언어 종류"),
        ;

        private final String pageId;
        private final String text;

        DocUrl(String pageId, String text) {
            this.pageId = pageId;
            this.text = text;
        }
    }
}
