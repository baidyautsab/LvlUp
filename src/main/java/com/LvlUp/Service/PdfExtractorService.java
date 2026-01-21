package com.LvlUp.Service;

import com.LvlUp.Entity.PdfExtractor;
import com.LvlUp.Entity.Quiz;
import com.LvlUp.Repository.PdfExtractorRepository;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PdfExtractorService {

    private final PdfExtractorRepository pdfRepository;
    private final QuizGenerationService quizService;

    public PdfExtractorService(
            PdfExtractorRepository pdfRepository,
            QuizGenerationService quizService
    ) {
        this.pdfRepository = pdfRepository;
        this.quizService = quizService;
    }

    public boolean existsByFileName(String fileName) {
        return pdfRepository.existsByFileName(fileName);
    }

    /**
     * Upload PDF → extract text → save PDF → generate quiz
     */
    @Transactional
    public PdfExtractor extractAndSave(MultipartFile file, String topic) {

        try {
            // 1️⃣ Convert MultipartFile to Resource
            InputStreamResource resource =
                    new InputStreamResource(file.getInputStream());

            // 2️⃣ Extract text using Tika
            TikaDocumentReader reader = new TikaDocumentReader(resource);

            String content = reader.get()
                    .stream()
                    .map(Document::getText)
                    .collect(Collectors.joining("\n"));

            // 3️⃣ Save PDF entity
            PdfExtractor pdf = new PdfExtractor();
            pdf.setFileName(file.getOriginalFilename());
            pdf.setTopic(topic);
            pdf.setExtractedText(content);

            PdfExtractor savedPdf = pdfRepository.save(pdf);

            // 4️⃣ Generate Quiz (creates quiz + questions)
            quizService.generateAndSaveQuiz(savedPdf);

            return savedPdf;

        } catch (IOException e) {
            throw new RuntimeException("Failed to process PDF file", e);
        }
    }

    public Optional<PdfExtractor> getFileByName(String fileName) {
        return pdfRepository.findByFileName(fileName);
    }

    public void deleteFileByName(String fileName) {
        pdfRepository.deleteByFileName(fileName);
    }

    public PdfExtractor updateTopic(String fileName, String newTopic) {
        PdfExtractor pdf = pdfRepository.findByFileName(fileName)
                .orElseThrow(() ->
                        new RuntimeException("File not found: " + fileName)
                );

        pdf.setTopic(newTopic);
        return pdfRepository.save(pdf);
    }
}
