package com.poolaeem.poolaeem.word.application;

import com.poolaeem.poolaeem.word.domain.WordLang;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WordCompletionService {
    Pageable pageable = PageRequest.of(0, 5);
    WordLang getLanguage();

    List<String> completeWord(String word);

}
