package com.poolaeem.poolaeem.word.application;

import com.poolaeem.poolaeem.word.domain.WordLang;
import com.poolaeem.poolaeem.word.domain.service.EnglishWordCompletionServiceImpl;
import com.poolaeem.poolaeem.word.domain.service.KoreanWordCompletionServiceImpl;
import com.poolaeem.poolaeem.word.infra.repository.EnglishWordRepository;
import com.poolaeem.poolaeem.word.infra.repository.KoreanWordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단위: 단어 서비스 전략 패턴 테스트")
class WordCompletionServiceStrategyTest {
    private WordCompletionServiceStrategy wordCompletionServiceStrategy;

    public WordCompletionServiceStrategyTest() {
        EnglishWordRepository englishWordRepository = Mockito.mock(EnglishWordRepository.class);
        KoreanWordRepository koreanWordRepository = Mockito.mock(KoreanWordRepository.class);
        List<WordCompletionService> wordCompletionServices =
                List.of(new EnglishWordCompletionServiceImpl(englishWordRepository),
                        new KoreanWordCompletionServiceImpl(koreanWordRepository));

        this.wordCompletionServiceStrategy = new WordCompletionServiceStrategy(wordCompletionServices);
    }

    @Test
    @DisplayName("lang이 존재하지 않고 word에 한글이 존재하면 한국어 단어 완성 서비스를 반환한다")
    void testGetKoreanWordCompletionService() {
        String word = "한글이 존재";
        WordLang wordLang = null;

        assertThat(wordCompletionServiceStrategy.findService(word, wordLang))
                .isInstanceOf(KoreanWordCompletionServiceImpl.class);
        assertThat(wordCompletionServiceStrategy.findService(word, wordLang))
                .isNotInstanceOf(EnglishWordCompletionServiceImpl.class);
    }

    @Test
    @DisplayName("lang이 존재하지 않고 word에 한글이 존재하지 않으면 영어 단어 완성 서비스를 반환한다")
    void testGetEnglishWordCompletionService() {
        String word = "only-english";
        WordLang wordLang = null;

        assertThat(wordCompletionServiceStrategy.findService(word, wordLang))
                .isInstanceOf(EnglishWordCompletionServiceImpl.class);
        assertThat(wordCompletionServiceStrategy.findService(word, wordLang))
                .isNotInstanceOf(KoreanWordCompletionServiceImpl.class);
    }

    @Test
    @DisplayName("lang이 존재하면 word에 한글 유무와는 상관없이 lang과 관련된 서비스가 반환된다.")
    void testGetWordCompletionServiceForExistWordLang() {
        String word = "only-english";
        WordLang wordLang = WordLang.KOREAN;

        assertThat(wordCompletionServiceStrategy.findService(word, wordLang))
                .isInstanceOf(KoreanWordCompletionServiceImpl.class);
        assertThat(wordCompletionServiceStrategy.findService(word, wordLang))
                .isNotInstanceOf(EnglishWordCompletionServiceImpl.class);
    }
}