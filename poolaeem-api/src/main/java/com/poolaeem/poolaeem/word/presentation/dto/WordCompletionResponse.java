package com.poolaeem.poolaeem.word.presentation.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WordCompletionResponse {
    private List<String> words;

    public WordCompletionResponse(List<String> words) {
        this.words = words;
    }
}
