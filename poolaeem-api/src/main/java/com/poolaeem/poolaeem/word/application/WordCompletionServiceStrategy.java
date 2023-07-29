package com.poolaeem.poolaeem.word.application;

import com.poolaeem.poolaeem.common.exception.word.NotSupportLanguageException;
import com.poolaeem.poolaeem.word.domain.WordLang;
import com.poolaeem.poolaeem.word.domain.validation.WordValidation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WordCompletionServiceStrategy {
    private final List<WordCompletionService> wordCompletionServices;

    public WordCompletionServiceStrategy(List<WordCompletionService> wordCompletionServices) {
        this.wordCompletionServices = wordCompletionServices;
    }

    public WordCompletionService findService(String word, WordLang reqLang) {
        if (reqLang == null) {
            return getWordCompletionService(getWordLang(word));
        }
        return getWordCompletionService(reqLang);
    }

    private WordCompletionService getWordCompletionService(WordLang wordLang) {
        return wordCompletionServices.stream()
                .filter(service -> service.getLanguage() == wordLang)
                .findFirst()
                .orElseThrow(() -> new NotSupportLanguageException(WordValidation.Message.NOT_SUPPORT_LANG));
    }

    private WordLang getWordLang(String word) {
        String koreanRegex = ".*[\\p{IsHangul}].*";
        if (word.matches(koreanRegex)) {
            return WordLang.KOREAN;
        }
        return WordLang.ENGLISH;
    }
}
