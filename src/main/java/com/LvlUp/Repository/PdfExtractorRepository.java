package com.LvlUp.Repository;

import com.LvlUp.Entity.PdfExtractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PdfExtractorRepository extends JpaRepository<PdfExtractor, Long> {

}
