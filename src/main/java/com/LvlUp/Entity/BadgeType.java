package com.LvlUp.Entity;

public enum BadgeType {
    // --- Consistency Badges ---
    FIRST_STEPS("Completed your first quiz"),
    STREAK_MASTER("Reached a 7-day playing streak"),
    DEDICATED_SCHOLAR("Reached a 30-day playing streak"),

    // --- Accuracy Badges ---
    SHARP_SHOOTER("Scored 100% on a quiz"),
    PRECISION_MASTER("Scored 100% on 10 different quizzes"),

    // --- Volume/Grind Badges ---
    KNOWLEDGE_SEEKER("Earned 1,000 Total XP"),
    TRIVIA_GOD("Earned 10,000 Total XP");

    private final String description;

    BadgeType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}