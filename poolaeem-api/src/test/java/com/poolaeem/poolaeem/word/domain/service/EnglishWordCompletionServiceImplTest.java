package com.poolaeem.poolaeem.word.domain.service;

import com.poolaeem.poolaeem.word.domain.WordLang;
import com.poolaeem.poolaeem.word.infra.repository.EnglishWordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("영단어 완성 서비스 테스트")
class EnglishWordCompletionServiceImplTest {
    @InjectMocks
    private EnglishWordCompletionServiceImpl wordCompletionService;
    @Mock
    private EnglishWordRepository englishWordRepository;

    @Test
    @DisplayName("영어 단어 완성 서비스의 언어는 영어를 반환한다.")
    void testGetWordLanguage() {
        assertThat(wordCompletionService.getLanguage()).isEqualTo(WordLang.ENGLISH);
    }

    @Test
    @DisplayName("영단어 완성 서비스의 목록 크기는 기본값 5이다.")
    void testCompleteWordsSize() {
        assertThat(wordCompletionService.pageable.getPageSize()).isEqualTo(5);
    }

    @Test
    @DisplayName("영단어 단어 완성 목록을 조회할 수 있다.")
    void testCompleteEnglishWord() {
        String word = "appl";

        List<String> mockWords = List.of("appl", "application", "apply", "apple", "applicable");
        given(englishWordRepository.findAllWordByWordLikeAndLimit(anyString(), any()))
                .willReturn(mockWords);

        List<String> words = wordCompletionService.completeWord(word);

        assertThat(words).hasSize(mockWords.size());
        assertThat(words.get(4)).isEqualTo(mockWords.get(4));

        verify(englishWordRepository, times(1)).findAllWordByWordLikeAndLimit(anyString(), any());
    }
}