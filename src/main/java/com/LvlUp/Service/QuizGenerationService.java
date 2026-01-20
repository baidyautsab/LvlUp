package com.LvlUp.Service;

import com.LvlUp.Entity.PdfExtractor;
import com.LvlUp.Entity.QuizQuestion;
import com.LvlUp.Repository.QuizQuestionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizGenerationService {
    private final ChatModel chatModel;
    private final QuizQuestionRepository quizRepository;
    private final ObjectMapper objectMapper;

    public QuizGenerationService(ChatModel chatModel, QuizQuestionRepository quizRepository) {
        this.chatModel = chatModel;
        this.quizRepository = quizRepository;
        this.objectMapper = new ObjectMapper();
    }
//public QuizGenerationService(ChatModel chatModel,
//                             QuizQuestionRepository quizRepository,
//                             @Value("${spring.ai.google.genai.api-key:MISSING}") String activeKey) {
//    this.chatModel = chatModel;
//    this.quizRepository = quizRepository;
//    this.objectMapper = new ObjectMapper();
//
//    // Safer debugging
//    if ("MISSING".equals(activeKey)) {
//        System.err.println("!!! ERROR: GEMINI_API_KEY is not set in Environment Variables !!!");
//    } else {
//        // Only substring if the key actually exists to avoid NullPointerException
//        String maskedKey = activeKey.length() > 8 ? activeKey.substring(0, 8) : "****";
//        System.out.println(">>> Success: API Key loaded. Starts with: " + maskedKey);
//    }
//}

    public List<QuizQuestion> generateAndSaveQuestions(PdfExtractor doc) {
        String systemInstructions = """
            You are an educational assistant. Based on the provided text, generate a list of 5 multiple-choice questions.
            Format your response STRICTLY as a JSON array of objects. 
            Each object must have these keys: questionText, optionA, optionB, optionC, optionD, correctOption, explanation.
            correctOption must be a single letter: 'A', 'B', 'C', or 'D'.
            """;

        String userPrompt = "Text to generate quiz from: " + doc.getExtractedText();

        // Call Gemini
        String responseJson = chatModel.call(systemInstructions + "\n" + userPrompt);

        // Clean the response (sometimes Gemini adds ```json ... ``` blocks)
        String cleanedJson = responseJson.replaceAll("```json", "").replaceAll("```", "").trim();

        try {
            // Parse JSON into a list of maps
            List<Map<String, String>> rawQuestions = objectMapper.readValue(cleanedJson, new TypeReference<>() {});
            List<QuizQuestion> quizQuestions = new ArrayList<>();

            for (Map<String, String> q : rawQuestions) {
                QuizQuestion question = new QuizQuestion();
                question.setQuestionText(q.get("questionText"));
                question.setOptionA(q.get("optionA"));
                question.setOptionB(q.get("optionB"));
                question.setOptionC(q.get("optionC"));
                question.setOptionD(q.get("optionD"));
                question.setCorrectOption(q.get("correctOption"));
                question.setExplanation(q.get("explanation"));
                question.setDocument(doc); // Link to the PDF record

                quizQuestions.add(question);
            }

            return quizRepository.saveAll(quizQuestions);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini response: " + e.getMessage());
        }
    }
}
