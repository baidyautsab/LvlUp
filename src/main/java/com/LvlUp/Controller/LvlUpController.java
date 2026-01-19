package com.LvlUp.Controller;

import com.LvlUp.Entity.PdfExtractor;
import com.LvlUp.Service.PdfExtractorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LvlUpController {
    private static final Logger log =
            LoggerFactory.getLogger(LvlUpController.class);

    private static final long START_TIME =
            ManagementFactory.getRuntimeMXBean().getStartTime();

    private final PdfExtractorService pdfService;

    // Use constructor injection
    public LvlUpController(PdfExtractorService pdfService) {
        this.pdfService = pdfService;
    }

    @GetMapping("/status")
    public Map<String, Object> getApplicationStatus() {

        log.info("Application status check requested");

        long uptimeMs = System.currentTimeMillis() - START_TIME;

        Map<String, Object> response = new HashMap<>();
        response.put("application", "LevelUp");
        response.put("status", "UP");
        response.put("startedAt", formatDate(START_TIME));
        response.put("uptimeSeconds", uptimeMs / 1000);
        response.put("timestamp", formatDate(System.currentTimeMillis()));

        log.debug("Status response prepared: {}", response);

        return response;
    }

    @PostMapping("/admin/upload-pdf")
    public ResponseEntity<PdfExtractor> uploadPdf(
            @RequestParam("file") MultipartFile file,
            @RequestParam("topic") String topic) {

        log.info("Received PDF upload request for topic: {}", topic);
        PdfExtractor savedDoc = pdfService.extractAndSave(file, topic);
        return ResponseEntity.ok(savedDoc);
    }

    private String formatDate(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date(time));
    }
}
