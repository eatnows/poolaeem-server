package com.poolaeem.poolaeem.word.presentation;

import com.poolaeem.poolaeem.common.response.ApiResponseDto;
import com.poolaeem.poolaeem.word.application.WordCompletionService;
import com.poolaeem.poolaeem.word.application.WordCompletionServiceStrategy;
import com.poolaeem.poolaeem.word.domain.WordLang;
import com.poolaeem.poolaeem.word.presentation.dto.WordCompletionResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WordCompletionController {
    private final WordCompletionServiceStrategy wordCompletionService;

    public WordCompletionController(WordCompletionServiceStrategy wordCompletionService) {
        this.wordCompletionService = wordCompletionService;
    }

    @GetMapping("/api/word/{word}/complete")
    public ApiResponseDto<WordCompletionResponse> completeEnglishWord(@PathVariable @NotBlank String word,
                                                                      @RequestParam(required = false) WordLang lang) {
        WordCompletionService service = wordCompletionService.findService(word, lang);
        List<String> words = service.completeWord(word);

        WordCompletionResponse response = new WordCompletionResponse(words);
        return ApiResponseDto.OK(response);
    }
}
