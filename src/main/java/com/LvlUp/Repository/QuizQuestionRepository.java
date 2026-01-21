package com.LvlUp.Repository;

import com.LvlUp.Entity.QuizQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {

    // For GUI pagination
    Page<QuizQuestion> findByQuizId(Long quizId, Pageable pageable);

    // Optional: fetch all questions of a quiz
    List<QuizQuestion> findByQuizId(Long quizId);
}
