package com.LvlUp.Service;

import com.LvlUp.Entity.*;
import com.LvlUp.Repository.UserBadgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BadgeService {

    @Autowired
    private UserBadgeRepository userBadgeRepository;

    /**
     * Evaluates all badge conditions after a quiz is completed.
     */
    @Transactional
    public void checkForNewBadges(User user, QuizAttempt currentAttempt) {
        UserProfile profile = user.getUserProfile();

        // 1. Check Consistency (Streaks)
        checkConsistencyBadges(user, profile);

        // 2. Check Accuracy (Score on this specific attempt)
        checkAccuracyBadges(user, currentAttempt);

        // 3. Check Progression (Total XP)
        checkProgressionBadges(user, profile);
    }

    private void checkConsistencyBadges(User user, UserProfile profile) {
        // Badge: First Steps
        assignBadgeIfMissing(user, BadgeType.FIRST_STEPS);

        // Badge: Streak Master (7 days)
        if (profile.getDailyStreak() >= 7) {
            assignBadgeIfMissing(user, BadgeType.STREAK_MASTER);
        }

        // Badge: Dedicated Scholar (30 days)
        if (profile.getDailyStreak() >= 30) {
            assignBadgeIfMissing(user, BadgeType.DEDICATED_SCHOLAR);
        }
    }

    private void checkAccuracyBadges(User user, QuizAttempt attempt) {
        // Calculate percentage: (Score / Max) * 100
        double percentage = ((double) attempt.getScoreAchieved() / attempt.getMaxPossibleScore()) * 100;

        // Badge: Sharp Shooter (100% on a quiz)
        if (percentage >= 100.0) {
            assignBadgeIfMissing(user, BadgeType.SHARP_SHOOTER);
        }

        // Note: For "Precision Master" (10 perfect quizzes), you would need to
        // query the QuizAttemptRepository count, which is heavier logic.
    }

    private void checkProgressionBadges(User user, UserProfile profile) {
        int xp = profile.getTotalXp();

        // Badge: Knowledge Seeker (1k XP)
        if (xp >= 1000) {
            assignBadgeIfMissing(user, BadgeType.KNOWLEDGE_SEEKER);
        }

        // Badge: Trivia God (10k XP)
        if (xp >= 10000) {
            assignBadgeIfMissing(user, BadgeType.TRIVIA_GOD);
        }
    }

    /**
     * Helper to safely assign a badge only if the user doesn't have it yet.
     */
    private void assignBadgeIfMissing(User user, BadgeType type) {
        if (!userBadgeRepository.existsByUserIdAndBadgeType(user.getId(), type)) {
            UserBadge newBadge = new UserBadge(user, type);
            userBadgeRepository.save(newBadge);

            // Optional: Log it or send a notification to frontend
            System.out.println("User " + user.getUsername() + " earned badge: " + type);
        }
    }
}