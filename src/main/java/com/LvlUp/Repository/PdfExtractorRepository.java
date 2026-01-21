package com.LvlUp.Repository;

import com.LvlUp.Entity.PdfExtractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PdfExtractorRepository extends JpaRepository<PdfExtractor, Long> {

    boolean existsByFileName(String fileName);

    Optional<PdfExtractor> findByFileName(String fileName);

    void deleteByFileName(String fileName);
}
