package com.poolaeem.poolaeem.word.domain.service;

import com.poolaeem.poolaeem.word.application.WordCompletionService;
import com.poolaeem.poolaeem.word.domain.WordLang;
import com.poolaeem.poolaeem.word.infra.repository.KoreanWordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KoreanWordCompletionServiceImpl implements WordCompletionService {

    private final KoreanWordRepository koreanWordRepository;

    public KoreanWordCompletionServiceImpl(KoreanWordRepository koreanWordRepository) {
        this.koreanWordRepository = koreanWordRepository;
    }

    @Override
    public WordLang getLanguage() {
        return WordLang.KOREAN;
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> completeWord(String word) {
        return koreanWordRepository.findAllWordByWordLikeAndLimit(word, pageable);
    }
}
