package com.poolaeem.poolaeem.word.infra.repository;

import com.poolaeem.poolaeem.word.domain.entity.EnglishWord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnglishWordRepository extends JpaRepository<EnglishWord, Long> {
    @Query("select e.word from EnglishWord  e where e.word like :word%")
    List<String> findAllWordByWordLikeAndLimit(String word, Pageable pageable);
}
