package com.LvlUp.Service;

import com.LvlUp.Dto.UserProfileDTO;
import com.LvlUp.Entity.*;
import com.LvlUp.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserProfileService {

    @Autowired private UserRepository userRepository;
    @Autowired private QuizAttemptRepository quizAttemptRepository;

    @Transactional(readOnly = true)
    public UserProfileDTO getUserProfile(String username) {
        // 1. Fetch User and Profile
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile profile = user.getUserProfile();

        // 2. Fetch History
        List<QuizAttempt> attempts = quizAttemptRepository.findByUserIdOrderByCompletedAtDesc(user.getId());

        // 3. Map to DTO
        UserProfileDTO dto = new UserProfileDTO();
        dto.setUsername(user.getUsername());

        if (profile != null) {
            dto.setFullName(profile.getFullName());
            dto.setEmail(profile.getEmail());
            dto.setTotalXp(profile.getTotalXp());
            dto.setCurrentLeague(profile.getCurrentLeague());
            // ... set other fields
        }

        // 4. Map History Items (Extracting Topic from PDF)
        List<UserProfileDTO.QuizHistoryItem> historyList = attempts.stream().map(attempt -> {
            UserProfileDTO.QuizHistoryItem item = new UserProfileDTO.QuizHistoryItem();

            item.setScore(attempt.getScoreAchieved());
            item.setMaxScore(attempt.getMaxPossibleScore());
            item.setPlayedAt(attempt.getCompletedAt());

            // Navigating the relationships: Attempt -> Quiz -> PDF -> Topic
            Quiz quiz = attempt.getQuiz();
            item.setQuizTitle(quiz.getTitle());
            if (quiz.getPdf() != null) {
                item.setTopicName(quiz.getPdf().getTopic());
            }

            return item;
        }).collect(Collectors.toList());

        dto.setPlayHistory(historyList);

        return dto;
    }
}