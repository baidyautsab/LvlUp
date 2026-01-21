package com.LvlUp.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pdf_extractor")
public class PdfExtractor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String fileName;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String extractedText;

    @Column(nullable = false)
    private String topic;

    @Column(updatable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();

    // One PDF → Many Quizzes
    @OneToMany(mappedBy = "pdf", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quiz> quizzes;

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getExtractedText() {
        return extractedText;
    }

    public String getTopic() {
        return topic;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setExtractedText(String extractedText) {
        this.extractedText = extractedText;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    // getters & setters
}
