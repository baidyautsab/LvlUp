package com.LvlUp.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "user_profile")
public class UserProfile {

    @Id
    private Long id; // Shared ID with User (Primary Key)

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // This links the PK of UserProfile to the PK of User
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "avatar_url")
    private String avatarUrl;

    // --- Gamification Stats ---

    @Column(nullable = false)
    private int totalXp = 0;

    @Column(nullable = false)
    private int currentLevel = 1;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeagueTier currentLeague = LeagueTier.BRONZE;

    @Column(nullable = false)
    private int dailyStreak = 0;

    @Column(name = "last_quiz_date")
    private LocalDateTime lastQuizDate;

    // --- Constructors ---

    public UserProfile() {
        // JPA requires no-arg constructor
    }

    public UserProfile(User user, String fullName, String email) {
        this.user = user;
        this.fullName = fullName;
        this.email = email;
        this.totalXp = 0;
        this.currentLevel = 1;
        this.currentLeague = LeagueTier.BRONZE;
        this.dailyStreak = 0;
    }

    // --- Business Logic Methods ---

    /**
     * Adds XP and automatically recalculates Level and League.
     * @param xp The amount of XP earned from a quiz.
     */
    public void addXp(int xp) {
        if (xp < 0) return; // Prevent negative XP
        this.totalXp += xp;
        checkLevelUp();
        checkLeaguePromotion();
    }

    /**
     * Updates the daily streak based on the last time a quiz was played.
     * Logic:
     * - Played today? -> No change.
     * - Played yesterday? -> Increment streak.
     * - Played before yesterday? -> Reset streak to 1.
     */
    public void updateStreak() {
        LocalDateTime now = LocalDateTime.now();

        if (this.lastQuizDate == null) {
            // First time ever playing
            this.dailyStreak = 1;
        } else {
            LocalDate today = now.toLocalDate();
            LocalDate lastDate = this.lastQuizDate.toLocalDate();

            long daysBetween = ChronoUnit.DAYS.between(lastDate, today);

            if (daysBetween == 0) {
                // Already played today, do nothing
                return;
            } else if (daysBetween == 1) {
                // Played yesterday, keep the streak going
                this.dailyStreak++;
            } else {
                // Missed a day (or more), reset streak
                this.dailyStreak = 1;
            }
        }

        // Update the timestamp to now
        this.lastQuizDate = now;
    }

    /**
     * Internal logic to increase level based on XP thresholds.
     * Strategy: Level Up every 1000 XP.
     */
    private void checkLevelUp() {
        int newLevel = 1 + (this.totalXp / 1000);
        if (newLevel > this.currentLevel) {
            this.currentLevel = newLevel;
            // You could return a boolean or trigger an event here if you wanted to notify the user
        }
    }

    /**
     * Internal logic to promote League based on XP.
     */
    private void checkLeaguePromotion() {
        if (this.totalXp >= 10000) {
            this.currentLeague = LeagueTier.DIAMOND;
        } else if (this.totalXp >= 5000) {
            this.currentLeague = LeagueTier.PLATINUM;
        } else if (this.totalXp >= 2500) {
            this.currentLeague = LeagueTier.GOLD;
        } else if (this.totalXp >= 1000) {
            this.currentLeague = LeagueTier.SILVER;
        } else {
            this.currentLeague = LeagueTier.BRONZE;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public int getTotalXp() { return totalXp; }
    // No setter for TotalXp to enforce use of addXp() logic

    public int getCurrentLevel() { return currentLevel; }

    public LeagueTier getCurrentLeague() { return currentLeague; }

    public int getDailyStreak() { return dailyStreak; }

    public LocalDateTime getLastQuizDate() { return lastQuizDate; }
    public void setLastQuizDate(LocalDateTime lastQuizDate) { this.lastQuizDate = lastQuizDate; }
}