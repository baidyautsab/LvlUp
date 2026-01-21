package com.LvlUp.Repository;

import com.LvlUp.Entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    // Get all quizzes generated from a specific PDF
    List<Quiz> findByPdfId(Long pdfId);
}
