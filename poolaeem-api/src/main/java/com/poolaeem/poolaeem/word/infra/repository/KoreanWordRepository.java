package com.poolaeem.poolaeem.word.infra.repository;

import com.poolaeem.poolaeem.word.domain.entity.KoreanWord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KoreanWordRepository extends JpaRepository<KoreanWord, Long> {
    @Query("select k.word from KoreanWord k where k.word like :word%")
    List<String> findAllWordByWordLikeAndLimit(String word, Pageable pageable);
}
