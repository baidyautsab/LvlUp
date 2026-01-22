package com.LvlUp.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_attempt")
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(nullable = false)
    private int scoreAchieved;

    @Column(nullable = false)
    private int maxPossibleScore;

    @Column(nullable = false)
    private LocalDateTime completedAt = LocalDateTime.now();

    // Extra stats for analysis
    private int correctAnswersCount;
    private long timeTakenSeconds; // How long they took to finish

    public QuizAttempt() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public int getScoreAchieved() {
        return scoreAchieved;
    }

    public void setScoreAchieved(int scoreAchieved) {
        this.scoreAchieved = scoreAchieved;
    }

    public int getMaxPossibleScore() {
        return maxPossibleScore;
    }

    public void setMaxPossibleScore(int maxPossibleScore) {
        this.maxPossibleScore = maxPossibleScore;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public int getCorrectAnswersCount() {
        return correctAnswersCount;
    }

    public void setCorrectAnswersCount(int correctAnswersCount) {
        this.correctAnswersCount = correctAnswersCount;
    }

    public long getTimeTakenSeconds() {
        return timeTakenSeconds;
    }

    public void setTimeTakenSeconds(long timeTakenSeconds) {
        this.timeTakenSeconds = timeTakenSeconds;
    }

    public double getPercentage() {
        return ((double) scoreAchieved / maxPossibleScore) * 100;
    }
}