package com.LvlUp.Service;

import com.LvlUp.Entity.PdfExtractor;
import com.LvlUp.Entity.Quiz;
import com.LvlUp.Entity.QuizQuestion;
import com.LvlUp.Repository.QuizQuestionRepository;
import com.LvlUp.Repository.QuizRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class QuizGenerationService {

    private final ChatModel chatModel;
    private final QuizRepository quizRepository;
    private final QuizQuestionRepository questionRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public QuizGenerationService(
            ChatModel chatModel,
            QuizRepository quizRepository,
            QuizQuestionRepository questionRepository
    ) {
        this.chatModel = chatModel;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }

    public Quiz generateAndSaveQuiz(PdfExtractor pdf) {

        // 1️⃣ Create Quiz
        Quiz quiz = new Quiz();
        quiz.setPdf(pdf);
        quiz.setTitle("Quiz on " + pdf.getTopic());
        quiz.setTotalQuestions(5);

        Quiz savedQuiz = quizRepository.save(quiz);

        // 2️⃣ Build prompt
        String systemInstructions = """
            You are an educational assistant.
            Generate exactly 5 multiple-choice questions from the given text.
            Respond STRICTLY as a JSON array.
            Each object must contain:
            questionText, optionA, optionB, optionC, optionD, correctOption, explanation.
            correctOption must be A, B, C, or D.
            """;

        String userPrompt = "Text:\n" + pdf.getExtractedText();

        // 3️⃣ Call AI
        String response = chatModel.call(systemInstructions + "\n" + userPrompt);

        String cleanedJson = response
                .replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();

        try {
            List<Map<String, String>> rawQuestions =
                    objectMapper.readValue(cleanedJson, new TypeReference<>() {});

            List<QuizQuestion> questions = new ArrayList<>();

            for (Map<String, String> q : rawQuestions) {
                QuizQuestion question = new QuizQuestion();
                question.setQuiz(savedQuiz);
                question.setQuestionText(q.get("questionText"));
                question.setOptionA(q.get("optionA"));
                question.setOptionB(q.get("optionB"));
                question.setOptionC(q.get("optionC"));
                question.setOptionD(q.get("optionD"));
                question.setCorrectOption(q.get("correctOption"));
                question.setExplanation(q.get("explanation"));
                question.setDifficulty("EASY");

                questions.add(question);
            }

            questionRepository.saveAll(questions);

            return savedQuiz;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }
}
