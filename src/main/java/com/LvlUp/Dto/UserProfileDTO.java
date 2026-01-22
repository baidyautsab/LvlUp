package com.LvlUp.Dto;

import com.LvlUp.Entity.LeagueTier;
import java.time.LocalDateTime;
import java.util.List;

public class UserProfileDTO {

    // --- Basic Info ---
    private String username;
    private String fullName;
    private String email;
    private String avatarUrl;

    // --- Gamification Stats ---
    private int totalXp;
    private int currentLevel;
    private LeagueTier currentLeague;
    private int dailyStreak;

    // --- The Play History List ---
    private List<QuizHistoryItem> playHistory;

    // --- Constructor ---
    public UserProfileDTO() {
        // Default constructor needed for serialization frameworks (like Jackson)
    }

    // --- Getters & Setters for UserProfileDTO ---

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getTotalXp() {
        return totalXp;
    }

    public void setTotalXp(int totalXp) {
        this.totalXp = totalXp;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public LeagueTier getCurrentLeague() {
        return currentLeague;
    }

    public void setCurrentLeague(LeagueTier currentLeague) {
        this.currentLeague = currentLeague;
    }

    public int getDailyStreak() {
        return dailyStreak;
    }

    public void setDailyStreak(int dailyStreak) {
        this.dailyStreak = dailyStreak;
    }

    public List<QuizHistoryItem> getPlayHistory() {
        return playHistory;
    }

    public void setPlayHistory(List<QuizHistoryItem> playHistory) {
        this.playHistory = playHistory;
    }

    // ==========================================
    // Inner Static Class for History Items
    // ==========================================
    public static class QuizHistoryItem {

        private String quizTitle;
        private String topicName; // Retrieved from PdfExtractor
        private int score;
        private int maxScore;
        private LocalDateTime playedAt;

        public QuizHistoryItem() {}

        // --- Getters & Setters for QuizHistoryItem ---

        public String getQuizTitle() {
            return quizTitle;
        }

        public void setQuizTitle(String quizTitle) {
            this.quizTitle = quizTitle;
        }

        public String getTopicName() {
            return topicName;
        }

        public void setTopicName(String topicName) {
            this.topicName = topicName;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getMaxScore() {
            return maxScore;
        }

        public void setMaxScore(int maxScore) {
            this.maxScore = maxScore;
        }

        public LocalDateTime getPlayedAt() {
            return playedAt;
        }

        public void setPlayedAt(LocalDateTime playedAt) {
            this.playedAt = playedAt;
        }
    }
}