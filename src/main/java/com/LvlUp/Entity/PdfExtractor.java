package com.LvlUp.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pdf_extractor")
public class PdfExtractor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Lob // Large Object for storing the full extracted text
    @Column(columnDefinition = "LONGTEXT")
    private String extractedText;

    @Column(nullable = false)
    private String topic; // e.g., "Java", "Kafka"

    @Column(updatable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();

    // Relationship: One PDF can have many Questions
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizQuestion> questions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtractedText() {
        return extractedText;
    }

    public void setExtractedText(String extractedText) {
        this.extractedText = extractedText;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public List<QuizQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuizQuestion> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "PdfExtractor{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", extractedText='" + extractedText + '\'' +
                ", topic='" + topic + '\'' +
                ", uploadedAt=" + uploadedAt +
                ", questions=" + questions +
                '}';
    }
}
