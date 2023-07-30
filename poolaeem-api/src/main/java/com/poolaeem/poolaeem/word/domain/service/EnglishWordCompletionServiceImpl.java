package com.poolaeem.poolaeem.word.domain.service;

import com.poolaeem.poolaeem.word.application.WordCompletionService;
import com.poolaeem.poolaeem.word.domain.WordLang;
import com.poolaeem.poolaeem.word.infra.repository.EnglishWordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnglishWordCompletionServiceImpl implements WordCompletionService {

    private final EnglishWordRepository englishWordRepository;

    public EnglishWordCompletionServiceImpl(EnglishWordRepository englishWordRepository) {
        this.englishWordRepository = englishWordRepository;
    }

    @Override
    public WordLang getLanguage() {
        return WordLang.ENGLISH;
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> completeWord(String word) {
        return englishWordRepository.findAllWordByWordLikeAndLimit(word, pageable);
    }
}
